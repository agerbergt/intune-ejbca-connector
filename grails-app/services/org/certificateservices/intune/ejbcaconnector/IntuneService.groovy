/**
 * Copyright (C) 2018 CGI Certificate Services
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.certificateservices.intune.ejbcaconnector

import com.microsoft.intune.scepvalidation.IntuneScepServiceClient
import com.microsoft.intune.scepvalidation.IntuneScepServiceException
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.bouncycastle.asn1.ASN1ObjectIdentifier
import org.bouncycastle.asn1.pkcs.Attribute
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.util.encoders.Base64
import org.jscep.transaction.TransactionId

import javax.annotation.PostConstruct
import javax.xml.bind.DatatypeConverter
import java.security.MessageDigest
import java.security.cert.X509Certificate

/**
 * Service responsible for validating requests with Microsoft Intune
 * and to send success/failure notifications.
 *
 * @author Tobias Agerberg
 */
@Transactional
class IntuneService {
    static final String CHALLENGE_PASS_ASN1_OID = "1.2.840.113549.1.9.7"

    EjbcaService ejbcaService
    GrailsApplication grailsApplication

    private IntuneScepServiceClient intuneScepServiceClient
    private static initialized

    @PostConstruct
    void init() {
        if(grailsApplication.config.intune.tenant && grailsApplication.config.intune.appId &&
                grailsApplication.config.intune.appKey && grailsApplication.config.ejbca.serviceName){
            Properties properties = new Properties()
            properties.setProperty("AAD_APP_ID", grailsApplication.config.intune.appId)
            properties.setProperty("AAD_APP_KEY", grailsApplication.config.intune.appKey)
            properties.setProperty("TENANT", grailsApplication.config.intune.tenant)
            properties.setProperty("PROVIDER_NAME_AND_VERSION", grailsApplication.config.ejbca.serviceName)

            try {
                log.info "Initializing connection to Intune (${grailsApplication.config.intune.tenant})."
                intuneScepServiceClient = new IntuneScepServiceClient(properties)
            } catch(Exception e){
                log.error "Failed to initialize Intune connection: ${e.message}"
                return
            }
        } else {
            log.error "Missing required configuration 'intune.tenant', 'intune.appId', 'intune.appKey' and/or 'ejbca.serviceName'."
            return
        }

        initialized = true
    }

    boolean validateRequest(PKCS10CertificationRequest request, TransactionId transactionId){
        boolean challengeIsValid = false
        boolean challengeExists = false
        String base64Request = new String(Base64.encode(request.encoded))

        log.info "Validating PKCS#10 request (Transaction ID: ${transactionId})"
        log.debug "PKCS#10 (Base64): ${base64Request}"

        for(Attribute a: request.getAttributes(new ASN1ObjectIdentifier(CHALLENGE_PASS_ASN1_OID))){
            challengeExists = true

            try {
                intuneScepServiceClient.ValidateRequest(transactionId.toString(), base64Request)
                log.info "Challenge in PKCS#10 request validated successfully!"
                challengeIsValid = true
            } catch(IntuneScepServiceException e){
                log.error "Request is not valid: ${e.message}"

                try {
                    intuneScepServiceClient.SendFailureNotification(transactionId.toString(), base64Request, 0x8000ffff, "CSR Failed validation: ${e.message}")
                } catch(Exception e2){
                    log.error "Error while sending failure notification: ${e2.message}"
                }
            } catch(Exception e){
                log.error "Error when validating request: ${e.message}"

                try {
                    intuneScepServiceClient.SendFailureNotification(transactionId.toString(), base64Request, 0x8000ffff, "Error when validating CSR: ${e.message}")
                } catch(Exception e2){
                    log.error "Error while sending failure notification: ${e2.message}"
                }
            }
        }

        if(!challengeExists){
            log.error "Challenge does not exist in PKCS#10 request (Transaction ID: ${transactionId}). Certificate enrollment will not be processed."
        } else if(!challengeIsValid){
            log.error "Challenge in PKCS#10 request (Transaction ID: ${transactionId}) is not valid. Certificate enrollment will not be processed."
        }

        return challengeIsValid
    }

    void completeRequest(PKCS10CertificationRequest request, X509Certificate certificate, TransactionId transactionId){
        try {
            log.debug "Completing request (Transaction ID: ${transactionId})"
            String base64Request = new String(Base64.encode(request.encoded))
            String thumbprint = DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA-1").digest(certificate.getEncoded())).toLowerCase()
            String serialnumber = certificate.getSerialNumber().toString(16).toLowerCase()
            String expiredate = certificate.getNotAfter().toInstant().toString()
            String certificateAuthority = ejbcaService.certificateAuthority
            log.debug "Sending success notification (Transaction ID: ${transactionId.toString()}, Thumbprint: ${thumbprint}, Serialnumber: ${serialnumber}, Expiredate: ${expiredate}, CA: ${certificateAuthority}"
            intuneScepServiceClient.SendSuccessNotification(transactionId.toString(), base64Request, thumbprint, serialnumber, expiredate, certificateAuthority)

            log.debug "Success notification sent successfully!"
        } catch(IntuneScepServiceException e){
            log.error "Error when sending success notification: ${e.message}"
        } catch(Exception e){
            log.error "Error occured: ${e.message}"
        }
    }

    static boolean isInitialized(){
        return initialized
    }
}

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

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.apache.commons.lang.RandomStringUtils
import org.apache.cxf.configuration.jsse.TLSClientParameters
import org.apache.cxf.endpoint.Client
import org.apache.cxf.frontend.ClientProxy
import org.apache.cxf.transport.http.HTTPConduit
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.util.encoders.Base64
import org.ejbca.core.protocol.ws.*

import javax.annotation.PostConstruct
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.SSLContext
import javax.xml.namespace.QName
import javax.xml.ws.BindingProvider
import java.security.KeyStore
import java.security.cert.X509Certificate

/**
 * Service responsible for requesting certificates from EJBCA
 *
 * @author Tobias Agerberg
 */
@Transactional
class EjbcaService {
    static final int EJBCA_USER_STATUS_NEW = 10
    static final String EJBCA_TOKEN_TYPE_USERGENERATED = "USERGENERATED"
    static final String EJBCA_RESPONSETYPE_CERTIFICATE = "CERTIFICATE"
    static final String EJBCA_WS_DEFAULT_SSL_ALGORITHM = "TLSv1.2"

    private String certificateAuthority
    private String certificateProfile
    private String endEntityProfile
    private String baseDN
    private EjbcaWS ejbcaWS
    private List<X509Certificate> cACertificates
    private static boolean initialized

    GrailsApplication grailsApplication

    @PostConstruct
    void init() {
        if(!grailsApplication.config.ejbca.keystorePath || !grailsApplication.config.ejbca.keystorePassword){
            throw new InvalidConfigurationException("Missing required configuration 'ejbca.keystorePath' and/or 'ejbca.keystorePassword'.")
        }

        if(grailsApplication.config.profile.certificateAuthority && grailsApplication.config.profile.certificateProfile &&
                grailsApplication.config.profile.endEntityProfile) {
            certificateAuthority = grailsApplication.config.profile.certificateAuthority
            certificateProfile = grailsApplication.config.profile.certificateProfile
            endEntityProfile = grailsApplication.config.profile.endEntityProfile
        } else {
            log.error "Missing required configuration 'profile.certificateAuthority', 'profile.certificateProfile' and/or 'profile.endEntityProfile'."
            return
        }

        if(grailsApplication.config.ejbca.serviceUrl){
            String serviceURL = grailsApplication.config.ejbca.serviceUrl
            String sslAlgorithm = (grailsApplication.config.ejbca.sslAlgorithm ?: EJBCA_WS_DEFAULT_SSL_ALGORITHM)

            try {
                log.info "Initializing connection to EJBCA (${serviceURL})."

                SSLContext sslContext = createSSLContext(
                        grailsApplication.config.ejbca.keystorePath,
                        grailsApplication.config.ejbca.keystorePassword,
                        sslAlgorithm
                )

                // Initialize web service client with WSDL from classpath first.
                URL wsdlLocation = this.class.classLoader.getResource("ejbcaws.wsdl")
                ejbcaWS = new EjbcaWSService(wsdlLocation, new QName("http://ws.protocol.core.ejbca.org/",
                        "EjbcaWSService")).ejbcaWSPort
                Client client = ClientProxy.getClient(ejbcaWS)
                HTTPConduit http = (HTTPConduit) client.getConduit()

                // Configure TLS parameters for client certificate authentication
                // and setup endpoint address to use from configuration.
                TLSClientParameters tlsClientParameters = new TLSClientParameters()
                tlsClientParameters.setSSLSocketFactory(sslContext.getSocketFactory())
                tlsClientParameters.setUseHttpsURLConnectionDefaultSslSocketFactory(false)
                http.setTlsClientParameters(tlsClientParameters)
                BindingProvider bindingProvider = (BindingProvider)ejbcaWS
                bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL)

                log.info "Connection to EJBCA initialized successfully! (${ejbcaWS.getEjbcaVersion()})"
            } catch(Exception e) {
                log.error "Unable to initialize connection to EJBCA. Check server and/or configuration and restart application (${e.message})"
                e.printStackTrace()
                return
            }
        } else {
            log.error "Missing required configuration 'ejbca.serviceUrl'."
            return
        }

        baseDN = grailsApplication.config.profile.baseDN
        initialized = true
    }

    private SSLContext createSSLContext(String keystorePath, String keystorePassword, String algorithm) {
        log.debug "Creating SSL Context for EJBCA WS connection (Keystore: ${keystorePath}, Algorithm: ${algorithm}"

        SSLContext context = SSLContext.getInstance(algorithm)

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray())
        keyManagerFactory.init(keyStore, keystorePassword.toCharArray())

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
        trustStore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray())
        trustManagerFactory.init(trustStore)

        context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null)

        return context
    }

    X509Certificate requestCertificate(PKCS10CertificationRequest request) {
        String subjectAltName = null
        String username = CertUtils.getSubjectDNField(request.subject.toString(), BCStyle.CN)
        String password = RandomStringUtils.random(16, true, true)
        String subjectDN = getSubjectDN(request)

        Extension sanExtension = CertUtils.getSubjectAlternativeNameExtensionFromCertificateRequest(request)
        String upn = CertUtils.getSubjectAlternativeNameFieldFromExtension(sanExtension, GeneralName.otherName)
        String email =  CertUtils.getSubjectAlternativeNameFieldFromExtension(sanExtension, GeneralName.rfc822Name)

        if(upn != null){
            subjectAltName = "UPN=${upn}"
        }

        if(email != null){
            if(subjectAltName != null){
                subjectAltName += ","
            }
            subjectAltName += "rfc822name=${email}"
        }

        UserDataVOWS user = new UserDataVOWS()
        user.setUsername(username)
        user.setPassword(password)
        user.setClearPwd(false)
        user.setSubjectDN(subjectDN)
        user.setCaName(getCertificateAuthority())
        user.setEmail(email)
        user.setSubjectAltName(subjectAltName)
        user.setStatus(EJBCA_USER_STATUS_NEW)
        user.setTokenType(EJBCA_TOKEN_TYPE_USERGENERATED)
        user.setEndEntityProfileName(getEndEntityProfile())
        user.setCertificateProfileName(getCertificateProfile())
        ejbcaWS.editUser(user)

        CertificateResponse response = ejbcaWS.pkcs10Request(username, password, new String(Base64.encode(request.encoded)), null, EJBCA_RESPONSETYPE_CERTIFICATE)
        return CertUtils.getX509CertificateFromByteArray(Base64.decode(response.data))
    }

    List<X509Certificate> getCACertificates() {
        if(cACertificates == null){
            try {
                log.debug "Retrieving certificate chain for CA (${certificateAuthority}) from EJBCA..."
                List<Certificate> chain = ejbcaWS.getLastCAChain(certificateAuthority)

                cACertificates = []
                chain.each {
                    cACertificates.add(CertUtils.getX509CertificateFromByteArray(Base64.decode(it.certificateData)))
                }
            } catch(Exception e){
                log.error "Failed to retrieve CA certificate chain from EJBCA: ${e.message}"
            }
        }
        return cACertificates
    }

    static boolean isInitialized() {
        return initialized
    }

    private String getSubjectDN(PKCS10CertificationRequest request) {
        return request.subject.toString() + (baseDN ? ",${baseDN}":"")
    }

    private String getCertificateAuthority() {
        return certificateAuthority
    }

    private String getEndEntityProfile() {
        return endEntityProfile
    }

    private String getCertificateProfile() {
        return certificateProfile
    }
}

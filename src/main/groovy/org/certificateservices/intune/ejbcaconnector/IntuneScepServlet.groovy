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

import groovy.util.logging.Slf4j
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.util.encoders.Base64
import org.jscep.server.ScepServlet
import org.jscep.transaction.FailInfo
import org.jscep.transaction.OperationFailureException
import org.jscep.transaction.TransactionId
import org.jscep.transport.response.Capability

import java.nio.file.Path
import java.nio.file.Paths
import java.security.PrivateKey
import java.security.cert.X509CRL
import java.security.cert.X509Certificate

/**
 * SCEP Servlet responsible for processing incoming SCEP requests
 * from devices managed through Intune.
 *
 * @author Tobias Agerberg
 */
@Slf4j
class IntuneScepServlet extends ScepServlet {
    IntuneService intuneService
    EjbcaService ejbcaService
    PrivateKey receiverPrivateKey
    X509Certificate[] receiverCertificateChain

    IntuneScepServlet(ConfigObject config){
        if(config.scep.keystorePath && config.scep.keystorePassword){
            receiverCertificateChain = CertUtils.getCertificatesFromJKS(config.scep.keystorePath) as X509Certificate[]
            receiverPrivateKey = CertUtils.getPrivateKeyFromJKS(config.scep.keystorePath, config.scep.keystorePassword, config.scep.keystoreAlias ?: null)
        } else {
            throw new InvalidConfigurationException("Missing required configuration 'config.scep.keystorePath' and/or 'config.scep.keystorePassword'.")
        }
    }

    @Override
    protected Set<Capability> doCapabilities(String intentifier) throws Exception {
        log.debug "PKI capabilites requested for indentifier: ${intentifier}"
        HashSet<Capability> capabilities = new HashSet<Capability>()
        capabilities.add(Capability.POST_PKI_OPERATION)
        capabilities.add(Capability.SHA_256)
        return capabilities
    }

    @Override
    protected List<X509Certificate> doEnrol(PKCS10CertificationRequest pkcs10req, TransactionId transactionId) throws Exception {
        List<X509Certificate> certificateChain = []

        if(intuneService.validateRequest(pkcs10req, transactionId)){
            log.info "Processing certificate request (Requested subject: ${pkcs10req.subject.toString()}, Transaction ID: ${transactionId}"
            X509Certificate certificate = ejbcaService.requestCertificate(pkcs10req)
            if(certificate != null){
                certificateChain.add(certificate)
                certificateChain.addAll(ejbcaService.getCACertificates())

                intuneService.completeRequest(pkcs10req, certificate, transactionId)
                log.info "Certificate issued successfully (Subject DN: ${certificate.subjectDN.toString()}, Transaction ID: ${transactionId}"
                if(log.isDebugEnabled()) {
                    log.debug "Certificate (Base64): ${new String(Base64.encode(certificate.encoded))}"
                }
            } else{
                throw new OperationFailureException("Certificate request failed (Transaction ID: " + transactionId.toString() + ")")
            }
        } else {
            throw new OperationFailureException("Invalid certificate request (Transaction ID: " + transactionId.toString() + ")")
        }

        return certificateChain
    }

    @Override
    protected List<X509Certificate> doGetCaCertificate(String identifier)
            throws Exception {
        log.info "CA certificate requested (Identifier: ${identifier})"
        List<X509Certificate> caCertificates = receiverCertificateChain.toList()

        if(log.isDebugEnabled()){
            caCertificates.each {
                log.debug "Returning Certificate with subject: ${it.subjectDN.toString()}"
            }
        }

        return caCertificates
    }

    /**
     * Returns the private key of the recipient entity represented by this SCEP
     * server.
     *
     * @return the private key.
     */
    @Override
    protected PrivateKey getRecipientKey() {
        return receiverPrivateKey
    }

    /**
     * Returns the certificate of the server recipient entity.
     *
     * @return the certificate.
     */
    @Override
    protected X509Certificate getRecipient() {
        if(receiverCertificateChain != null && receiverCertificateChain.length > 0){
            return receiverCertificateChain[0]
        }

        return null
    }

    /**
     * Returns the private key of the entity represented by this SCEP server.
     *
     * @return the private key.
     */
    @Override
    protected PrivateKey getSignerKey() {
        return receiverPrivateKey
    }

    /**
     * Returns the certificate of the entity represented by this SCEP server.
     *
     * @return the certificate.
     */
    @Override
    protected X509Certificate getSigner() {
        return receiverCertificateChain[0]
    }

    /**
     * Returns the certificate chain of the entity represented by this SCEP server.
     *
     * @return the chain
     */
    @Override
    protected X509Certificate[] getSignerCertificateChain() {
        return receiverCertificateChain
    }

    @Override
    protected List<X509Certificate> getNextCaCertificate(String identifier) throws Exception {
        // Not implemented
        throw new OperationFailureException(FailInfo.badRequest)
    }

    @Override
    protected List<X509Certificate> doGetCert(X500Name issuer, BigInteger serial) throws Exception {
        // Not implemented
        throw new OperationFailureException(FailInfo.badRequest)
    }

    @Override
    protected List<X509Certificate> doGetCertInitial(X500Name issuer, X500Name subject, TransactionId transId) throws Exception {
        // Not implemented
        throw new OperationFailureException(FailInfo.badRequest)
    }

    @Override
    protected X509CRL doGetCrl(X500Name issuer, BigInteger serial) throws Exception {
        // Not implemented
        throw new OperationFailureException(FailInfo.badRequest)
    }
}

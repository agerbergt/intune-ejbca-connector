package org.certificateservices.intune.ejbcaconnector

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.apache.commons.lang.RandomStringUtils
import org.apache.http.ssl.SSLContexts
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.util.encoders.Base64
import org.ejbca.core.protocol.ws.Certificate
import org.ejbca.core.protocol.ws.CertificateResponse
import org.ejbca.core.protocol.ws.EjbcaWS
import org.ejbca.core.protocol.ws.EjbcaWSService
import org.ejbca.core.protocol.ws.UserDataVOWS

import javax.annotation.PostConstruct
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.wsdl.WSDLException
import javax.xml.namespace.QName
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate

@Transactional
class EjbcaService {
    static final int EJBCA_USER_STATUS_NEW = 10
    static final String EJBCA_TOKEN_TYPE_USERGENERATED = "USERGENERATED"
    static final String EJBCA_RESPONSETYPE_CERTIFICATE = "CERTIFICATE"

    private String defaultCertificateAuthority
    private String defaultCertificateProfile
    private String defaultEndEntityProfile
    private String defaultBaseDN
    private EjbcaWS ejbcaWS
    private List<X509Certificate> cACertificates
    private static boolean initialized

    GrailsApplication grailsApplication

    @PostConstruct
    void init() {
        if(grailsApplication.config.ejbca.keystorePath && grailsApplication.config.ejbca.keystorePassword){
            HttpsURLConnection.setDefaultSSLSocketFactory(getSSLSocketFactory(
                    grailsApplication.config.ejbca.keystorePath, grailsApplication.config.ejbca.keystorePassword))
        } else {
            log.error "Missing required configuration 'ejbca.keystorePath' and/or 'ejbca.keystorePassword'."
            return
        }

        if(grailsApplication.config.profile.certificateAuthority && grailsApplication.config.profile.certificateProfile &&
                grailsApplication.config.profile.endEntityProfile) {
            defaultCertificateAuthority = grailsApplication.config.profile.certificateAuthority
            defaultCertificateProfile = grailsApplication.config.profile.certificateProfile
            defaultEndEntityProfile = grailsApplication.config.profile.endEntityProfile
        } else {
            log.error "Missing required configuration 'profile.certificateAuthority', 'profile.certificateProfile' and/or 'profile.endEntityProfile'."
            return
        }

        if(grailsApplication.config.ejbca.serviceUrl){
            try {
                log.info "Initializing connection to EJBCA (${grailsApplication.config.ejbca.serviceUrl})."
                ejbcaWS = new EjbcaWSService(new URL(grailsApplication.config.ejbca.serviceUrl),
                        new QName("http://ws.protocol.core.ejbca.org/", "EjbcaWSService")).ejbcaWSPort
            } catch(Exception e) {
                log.error "Unable to initialize connection to EJBCA. Check server and/or configuration and restart application (${e.message})"
                return
            }
        } else {
            log.error "Missing required configuration 'ejbca.serviceUrl'."
            return
        }

        defaultBaseDN = grailsApplication.config.profile.baseDN
        initialized = true
    }

    private SSLSocketFactory getSSLSocketFactory(String keystorePath, String keystorePassword ) {
        KeyStore keyStore = KeyStore.getInstance("JKS")
        keyStore.load(new FileInputStream(new File(keystorePath)), keystorePassword.toCharArray())

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
        keyManagerFactory.init(keyStore, keystorePassword.toCharArray())
        SSLContext context = SSLContext.getInstance("TLS")
        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom())
        return context.getSocketFactory()
    }

    X509Certificate requestCertificate(PKCS10CertificationRequest request, String enrollmentProfile) {
        String subjectAltName
        String username = CertUtils.getSubjectDNField(request.subject.toString(), BCStyle.CN)
        String password = RandomStringUtils.random(16, true, true)
        String subjectDN = getSubjectDN(request, enrollmentProfile)

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
        user.setCaName(getCertificateAuthority(enrollmentProfile))
        user.setEmail(email)
        user.setSubjectAltName(subjectAltName)
        user.setStatus(EJBCA_USER_STATUS_NEW)
        user.setTokenType(EJBCA_TOKEN_TYPE_USERGENERATED)
        user.setEndEntityProfileName(getEndEntityProfile(enrollmentProfile))
        user.setCertificateProfileName(getCertificateProfile(enrollmentProfile))
        ejbcaWS.editUser(user)

        CertificateResponse response = ejbcaWS.pkcs10Request(username, password, new String(Base64.encode(request.encoded)), null, EJBCA_RESPONSETYPE_CERTIFICATE)
        return CertUtils.getX509CertificateFromByteArray(Base64.decode(response.data))
    }

    List<X509Certificate> getCACertificates(String enrollmentProfile) {
        if(cACertificates == null){
            try {
                log.debug "Retrieving certificate chain for CA (${defaultCertificateAuthority}) from EJBCA..."
                List<Certificate> chain = ejbcaWS.getLastCAChain(defaultCertificateAuthority)

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

    private String getSubjectDN(PKCS10CertificationRequest request, String enrollmentProfile) {
        String baseDN = defaultBaseDN
        if(enrollmentProfile != null){
            baseDN = grailsApplication.config.getProperty("customProfiles.${enrollmentProfile}.baseDN") ?: baseDN
        }
        return request.subject.toString() + (baseDN ? ",${baseDN}":"")
    }

    private String getCertificateAuthority(enrollmentProfile) {
        String certificateAuthority = defaultCertificateAuthority
        if(enrollmentProfile != null){
            certificateAuthority = grailsApplication.config.getProperty("customProfiles.${enrollmentProfile}.certificateAuthority")
            if(certificateAuthority == null){
                throw new InvalidConfigurationException("Missing configuration 'certificateAuthority' for custom profile '${enrollmentProfile}'")
            }
        }
        return certificateAuthority
    }

    private String getEndEntityProfile(enrollmentProfile) {
        String endEntityProfile = defaultEndEntityProfile
        if(enrollmentProfile != null){
            endEntityProfile = grailsApplication.config.getProperty("customProfiles.${enrollmentProfile}.endEntityProfile")
            if(endEntityProfile == null){
                throw new InvalidConfigurationException("Missing configuration 'endEntityProfile' for custom profile '${enrollmentProfile}'")
            }
        }
        return endEntityProfile
    }

    private String getCertificateProfile(enrollmentProfile) {
        String certificateProfile = defaultCertificateProfile
        if(enrollmentProfile != null){
            certificateProfile = grailsApplication.config.getProperty("customProfiles.${enrollmentProfile}.certificateProfile")
            if(certificateProfile == null){
                throw new InvalidConfigurationException("Missing configuration 'certificateProfile' for custom profile '${enrollmentProfile}'")
            }
        }
        return certificateProfile
    }
}

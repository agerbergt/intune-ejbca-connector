package org.certificateservices.intune.ejbcaconnector

import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.util.encoders.Base64
import spock.lang.Specification

import java.security.MessageDigest
import java.security.PrivateKey
import java.security.cert.X509Certificate

class CertUtilsSpec extends Specification {

    def "test getCertificatesFromJKS"(){
        when:
        def certs = CertUtils.getCertificatesFromJKS("src/test/resources/scepreceiver.jks")

        then:
        certs != null
        certs.size() == 3
    }

    def "test methods to get subject alternative name"() {
        when:
        PKCS10CertificationRequest request = CertUtils.getPKCS10CertificationRequestFromFile("src/test/resources/samplerequest.csr")

        Extension sanExtension = CertUtils.getSubjectAlternativeNameExtensionFromCertificateRequest(request)
        String upn = CertUtils.getSubjectAlternativeNameFieldFromExtension(sanExtension, GeneralName.otherName)

        then:
        sanExtension != null
        upn == "admin@enterprisejavabeans.onmicrosoft.com"
    }

    def "test getSubjectDNField"() {
        when:
        X509Certificate certificate = CertUtils.getCertificatesFromJKS("src/test/resources/scepreceiver.jks").first()
        String cn = CertUtils.getSubjectDNField(certificate.subjectDN.name, BCStyle.CN)

        then:
        cn == "scepreceiver"
    }

    def "test getPrivateKeyFromJKS"() {
        when:
        PrivateKey privateKey = CertUtils.getPrivateKeyFromJKS("src/test/resources/scepreceiver.jks", "foo123", "scepreceiver")
        String privateKeyHash = new String(Base64.encode(MessageDigest.getInstance("SHA-256").digest(privateKey.encoded)), "UTF-8")
        String expectedHash = "wJ2dkZqh3mJZYZofu1EsrMyjXk9a8UXc00QHBQTBgXI="
        then:
        privateKey != null
        privateKeyHash == expectedHash
    }

    def "test getPKCS10CertificationRequestFromFile"() {
        when:
        PKCS10CertificationRequest request = CertUtils.getPKCS10CertificationRequestFromFile("src/test/resources/samplerequest.csr")

        then:
        request != null
        request.subject.toString() == "CN=admin@enterprisejavabeans.onmicrosoft.com"
    }

    def "test getX509CertificateFromByteArray"() {
        when:
        byte[] encoded = CertUtils.getCertificatesFromJKS("src/test/resources/scepreceiver.jks").first().encoded
        X509Certificate certificate = CertUtils.getX509CertificateFromByteArray(encoded)

        then:
        certificate != null
        certificate.subjectDN.name == "C=SE, O=Lab Certificate Services Org, CN=scepreceiver"
    }
}

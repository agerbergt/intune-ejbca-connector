package org.certificateservices.intune.ejbcaconnector

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class EjbcaServiceSpec extends Specification implements ServiceUnitTest<EjbcaService>{
    static String testConfiguration = """
    ejbca.serviceName = "EJBCA 6.3.1.1"
    ejbca.serviceUrl = "https://ca1.lab.certificateservices.org:8443/ejbca/ejbcaws/ejbcaws?wsdl"
    ejbca.keystorePath = "/opt/intune-ejbca-connector/intunera.jks"
    ejbca.keystorePassword = "foo123"
    ejbca.defaultCertificateAuthority = "LCSO_MobileCA"
    ejbca.defaultCertificateProfile = "CP_LCSO_Mobile"
    ejbca.defaultEndEntityProfile = "EEP_LCSO_Mobile"
    ejbca.defaultBaseDN = "O=Lab Certificate Services Org,C=SE"
    """

    def setup() {
    }

    def cleanup() {
    }
}

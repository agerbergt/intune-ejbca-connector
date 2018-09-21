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

import org.bouncycastle.asn1.*
import org.bouncycastle.asn1.pkcs.Attribute
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.bouncycastle.asn1.x500.AttributeTypeAndValue
import org.bouncycastle.asn1.x500.RDN
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.Extensions
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.pkcs.PKCS10CertificationRequest

import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

/**
 * Class containing certificate related utilities
 *
 * @author Tobias Agerberg
 * @author Philip Vendil
 */
class CertUtils {
    static final String UPN_OBJECTID = "1.3.6.1.4.1.311.20.2.3"

    /**
     * Method to fetch x509 certificate extension that contains alternative names in csr
     * @param request which contains alternative names in form of extensions
     * @return x509 extension
     */
    static Extension getSubjectAlternativeNameExtensionFromCertificateRequest(PKCS10CertificationRequest request){
        Extension subAltExtension = null

        if(request!= null){
            Attribute[] attrs = request.getAttributes(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest)

            if(attrs != null && attrs.length>0){
                Attribute extensionAttribute = attrs[0]
                ASN1Encodable[] encodableAttributeValues = extensionAttribute.getAttributeValues()
                if(encodableAttributeValues!= null && encodableAttributeValues.length>0){
                    ASN1Encodable encodableAttributeValue = encodableAttributeValues[0]
                    if(encodableAttributeValue instanceof DERSequence){
                        DERSequence seq = (DERSequence) encodableAttributeValue
                        Extensions ex = Extensions.getInstance(seq)
                        subAltExtension = ex.getExtension(Extension.subjectAlternativeName)
                    }
                }
            }
        }

        return subAltExtension
    }

    /**
     * Method to read a subject alternative name field from an extension.
     * @param extension Extension containing subject alternative names
     * @param generalNameID identifier of field to read (Use constants in
     * org.bouncycastle.asn1.x509.GeneralName class)
     * @return Value of subject alternative name field
     */
    static String getSubjectAlternativeNameFieldFromExtension(Extension extension, int generalNameID){
        String field = null

        if(extension != null && generalNameID >= 0 && generalNameID <= 8){
            ASN1Sequence sequence = getAltnameSequence(extension.extnValue.getOctets())

            Enumeration it = sequence.getObjects()
            while (it.hasMoreElements()){

                GeneralName genName = GeneralName.getInstance(it.nextElement())
                if(genName.getTagNo()==generalNameID ){
                    if(generalNameID == GeneralName.rfc822Name || generalNameID == GeneralName.dNSName  || generalNameID == GeneralName.uniformResourceIdentifier){
                        ASN1String name= (ASN1String) (genName.getName())
                        field = name.toString()

                    }

                    if(generalNameID == GeneralName.directoryName){
                        field = X500Name.getInstance(genName.getName()).toString()
                    }

                    if(generalNameID == GeneralName.otherName){
                        ASN1Primitive prim = genName.getName().toASN1Primitive()

                        if (prim instanceof DLSequence) {
                            DLSequence pr = ( DLSequence)prim
                            field = getUPNStringFromSequence(pr)
                        }
                    }
                    break
                }
            }
        }

        return field
    }

    /**
     * Returns first field value of a X500 name given the asn1 oid.
     *
     * Example: getSubjectDNField("CN=Test User,O=TestOrt", BSStyle.CN) == "Test User"
     *
     * @param subject the X500 name to parse a given field value of
     * @param fieldName Should be one of BCStyle field constants
     * @return the first found field value in the X500 name or null if no field value was found.
     */
    static String getSubjectDNField(String subject, ASN1ObjectIdentifier fieldName){
        if(subject == null){
            return null
        }
        for(RDN rDN : new X500Name(BCStyle.INSTANCE,subject).getRDNs(fieldName)){
            AttributeTypeAndValue first = rDN.getFirst()
            return first.getValue().toString()
        }

        return null
    }

    /**
     * Return list of all certificates found in a java key store. Certificate(s)
     * from key entries are placed first in the list.
     * @param keystorePath Path to keystore.
     * @return List of all certificates found in keystore, with certificates from key entries first.
     */
    static List<X509Certificate> getCertificatesFromJKS(String keystorePath){
        List<X509Certificate> certificates = []
        KeyStore ks = KeyStore.getInstance("JKS")
        ks.load(new FileInputStream(keystorePath), null)
        ks.aliases().each {
            if(ks.isKeyEntry(it)){
                certificates.add(0, (X509Certificate)ks.getCertificate(it))
            } else {
                certificates.add((X509Certificate)ks.getCertificate(it))
            }
        }
        return certificates
    }

    /**
     * Return reference to private key from java key store.
     * @param keystorePath Path to keystore.
     * @param keystorePassword Password protecting both keystore and key.
     * @param keystoreAlias Alias of key to return, may be null.
     * @return Private key from keystore with the given alias, or the first private key found if alias is null.
     */
    static PrivateKey getPrivateKeyFromJKS(String keystorePath, String keystorePassword, String keystoreAlias){
        KeyStore ks = KeyStore.getInstance("JKS")
        ks.load(new FileInputStream(keystorePath), keystorePassword.toCharArray())

        if(keystoreAlias == null){
            ks.aliases().each {
                if(ks.isKeyEntry(it)){
                    keystoreAlias = it
                }
            }
        }

        return ks.getKey(keystoreAlias, keystorePassword.toCharArray())
    }

    /**
     * Get PKCS10CertificationRequest from file containing PKCS#10 in PEM format
     * @param path Path to PKCS#10 PEM file
     * @return PKCS10CertificationRequest object
     */
    static PKCS10CertificationRequest getPKCS10CertificationRequestFromFile(String path) {
        PEMParser pemParser = new PEMParser(new FileReader(path))
        PKCS10CertificationRequest request = pemParser.readObject()
        return request
    }

    /**
     * Create X509Certificate from byte array with DER encoded certificate data
     * @param encoded DER encoded certificate
     * @return X509Certificate object
     */
    static X509Certificate getX509CertificateFromByteArray(byte[] encoded){
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509")
        InputStream inputStream = new ByteArrayInputStream(encoded)
        return (X509Certificate)certificateFactory.generateCertificate(inputStream)
    }


    /**
     * Help method for fetching alternative name from an ASN1Sequence
     */
    private static ASN1Sequence getAltnameSequence(byte[] value) throws IOException {
        ASN1Primitive oct = null
        try {
            oct = (new ASN1InputStream(new ByteArrayInputStream(value)).readObject())
        } catch (IOException e) {
            throw new RuntimeException("Could not read ASN1InputStream", e)
        }
        if (oct instanceof ASN1TaggedObject) {
            oct = ((ASN1TaggedObject)oct).getObject()
        }
        ASN1Sequence seq = ASN1Sequence.getInstance(oct)
        return seq
    }

    /**
     * Helper method for fetching the UPN alternative name.
     * @param seq the OtherName sequence
     */
    private static String getUPNStringFromSequence(ASN1Sequence seq) {
        if ( seq != null) {
            ASN1ObjectIdentifier id = ASN1ObjectIdentifier.getInstance(seq.getObjectAt(0))
            if (id.getId().equals(UPN_OBJECTID)) {
                ASN1TaggedObject obj = (ASN1TaggedObject) seq.getObjectAt(1)
                if(obj.getObject() instanceof ASN1TaggedObject){
                    obj =  (ASN1TaggedObject) obj.getObject()
                }
                DERUTF8String str = DERUTF8String.getInstance(obj.getObject())
                return str.getString()

            }
        }
        return null
    }
}

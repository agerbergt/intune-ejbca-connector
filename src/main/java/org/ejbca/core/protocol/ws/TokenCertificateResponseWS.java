
package org.ejbca.core.protocol.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tokenCertificateResponseWS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tokenCertificateResponseWS"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="certificate" type="{http://ws.protocol.core.ejbca.org/}certificate" minOccurs="0"/&gt;
 *         &lt;element name="keyStore" type="{http://ws.protocol.core.ejbca.org/}keyStore" minOccurs="0"/&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tokenCertificateResponseWS", propOrder = {
    "certificate",
    "keyStore",
    "type"
})
@XmlSeeAlso({
    Certificate.class,
    KeyStore.class
})
public class TokenCertificateResponseWS {

    protected Certificate certificate;
    protected KeyStore keyStore;
    protected int type;

    /**
     * Gets the value of the certificate property.
     * 
     * @return
     *     possible object is
     *     {@link Certificate }
     *     
     */
    public Certificate getCertificate() {
        return certificate;
    }

    /**
     * Sets the value of the certificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Certificate }
     *     
     */
    public void setCertificate(Certificate value) {
        this.certificate = value;
    }

    /**
     * Gets the value of the keyStore property.
     * 
     * @return
     *     possible object is
     *     {@link KeyStore }
     *     
     */
    public KeyStore getKeyStore() {
        return keyStore;
    }

    /**
     * Sets the value of the keyStore property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyStore }
     *     
     */
    public void setKeyStore(KeyStore value) {
        this.keyStore = value;
    }

    /**
     * Gets the value of the type property.
     * 
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     */
    public void setType(int value) {
        this.type = value;
    }

}

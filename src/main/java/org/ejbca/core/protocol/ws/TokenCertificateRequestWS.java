
package org.ejbca.core.protocol.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tokenCertificateRequestWS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tokenCertificateRequestWS"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="certificateProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyalg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyspec" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pkcs10Data" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="tokenType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="validityIdDays" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tokenCertificateRequestWS", propOrder = {
    "caName",
    "certificateProfileName",
    "keyalg",
    "keyspec",
    "pkcs10Data",
    "tokenType",
    "type",
    "validityIdDays"
})
public class TokenCertificateRequestWS {

    @XmlElement(name = "CAName")
    protected String caName;
    protected String certificateProfileName;
    protected String keyalg;
    protected String keyspec;
    protected byte[] pkcs10Data;
    protected String tokenType;
    protected int type;
    protected String validityIdDays;

    /**
     * Gets the value of the caName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAName() {
        return caName;
    }

    /**
     * Sets the value of the caName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAName(String value) {
        this.caName = value;
    }

    /**
     * Gets the value of the certificateProfileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateProfileName() {
        return certificateProfileName;
    }

    /**
     * Sets the value of the certificateProfileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateProfileName(String value) {
        this.certificateProfileName = value;
    }

    /**
     * Gets the value of the keyalg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyalg() {
        return keyalg;
    }

    /**
     * Sets the value of the keyalg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyalg(String value) {
        this.keyalg = value;
    }

    /**
     * Gets the value of the keyspec property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyspec() {
        return keyspec;
    }

    /**
     * Sets the value of the keyspec property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyspec(String value) {
        this.keyspec = value;
    }

    /**
     * Gets the value of the pkcs10Data property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPkcs10Data() {
        return pkcs10Data;
    }

    /**
     * Sets the value of the pkcs10Data property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPkcs10Data(byte[] value) {
        this.pkcs10Data = value;
    }

    /**
     * Gets the value of the tokenType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Sets the value of the tokenType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokenType(String value) {
        this.tokenType = value;
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

    /**
     * Gets the value of the validityIdDays property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidityIdDays() {
        return validityIdDays;
    }

    /**
     * Sets the value of the validityIdDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidityIdDays(String value) {
        this.validityIdDays = value;
    }

}

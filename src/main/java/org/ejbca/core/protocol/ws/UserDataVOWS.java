
package org.ejbca.core.protocol.ws;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for userDataVOWS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="userDataVOWS"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="caName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="certificateProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="certificateSerialNumber" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="clearPwd" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="endEntityProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extendedInformation" type="{http://ws.protocol.core.ejbca.org/}extendedInformationWS" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="hardTokenIssuerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyRecoverable" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sendNotification" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="subjectAltName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="subjectDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tokenType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userDataVOWS", propOrder = {
    "caName",
    "cardNumber",
    "certificateProfileName",
    "certificateSerialNumber",
    "clearPwd",
    "email",
    "endEntityProfileName",
    "endTime",
    "extendedInformation",
    "hardTokenIssuerName",
    "keyRecoverable",
    "password",
    "sendNotification",
    "startTime",
    "status",
    "subjectAltName",
    "subjectDN",
    "tokenType",
    "username"
})
public class UserDataVOWS {

    protected String caName;
    protected String cardNumber;
    protected String certificateProfileName;
    protected BigInteger certificateSerialNumber;
    protected boolean clearPwd;
    protected String email;
    protected String endEntityProfileName;
    protected String endTime;
    @XmlElement(nillable = true)
    protected List<ExtendedInformationWS> extendedInformation;
    protected String hardTokenIssuerName;
    protected boolean keyRecoverable;
    protected String password;
    protected boolean sendNotification;
    protected String startTime;
    protected int status;
    protected String subjectAltName;
    protected String subjectDN;
    protected String tokenType;
    protected String username;

    /**
     * Gets the value of the caName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaName() {
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
    public void setCaName(String value) {
        this.caName = value;
    }

    /**
     * Gets the value of the cardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the value of the cardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardNumber(String value) {
        this.cardNumber = value;
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
     * Gets the value of the certificateSerialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCertificateSerialNumber() {
        return certificateSerialNumber;
    }

    /**
     * Sets the value of the certificateSerialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCertificateSerialNumber(BigInteger value) {
        this.certificateSerialNumber = value;
    }

    /**
     * Gets the value of the clearPwd property.
     * 
     */
    public boolean isClearPwd() {
        return clearPwd;
    }

    /**
     * Sets the value of the clearPwd property.
     * 
     */
    public void setClearPwd(boolean value) {
        this.clearPwd = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the endEntityProfileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndEntityProfileName() {
        return endEntityProfileName;
    }

    /**
     * Sets the value of the endEntityProfileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndEntityProfileName(String value) {
        this.endEntityProfileName = value;
    }

    /**
     * Gets the value of the endTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndTime(String value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the extendedInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extendedInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtendedInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtendedInformationWS }
     * 
     * 
     */
    public List<ExtendedInformationWS> getExtendedInformation() {
        if (extendedInformation == null) {
            extendedInformation = new ArrayList<ExtendedInformationWS>();
        }
        return this.extendedInformation;
    }

    /**
     * Gets the value of the hardTokenIssuerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHardTokenIssuerName() {
        return hardTokenIssuerName;
    }

    /**
     * Sets the value of the hardTokenIssuerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHardTokenIssuerName(String value) {
        this.hardTokenIssuerName = value;
    }

    /**
     * Gets the value of the keyRecoverable property.
     * 
     */
    public boolean isKeyRecoverable() {
        return keyRecoverable;
    }

    /**
     * Sets the value of the keyRecoverable property.
     * 
     */
    public void setKeyRecoverable(boolean value) {
        this.keyRecoverable = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the sendNotification property.
     * 
     */
    public boolean isSendNotification() {
        return sendNotification;
    }

    /**
     * Sets the value of the sendNotification property.
     * 
     */
    public void setSendNotification(boolean value) {
        this.sendNotification = value;
    }

    /**
     * Gets the value of the startTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartTime(String value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the status property.
     * 
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     */
    public void setStatus(int value) {
        this.status = value;
    }

    /**
     * Gets the value of the subjectAltName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectAltName() {
        return subjectAltName;
    }

    /**
     * Sets the value of the subjectAltName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectAltName(String value) {
        this.subjectAltName = value;
    }

    /**
     * Gets the value of the subjectDN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectDN() {
        return subjectDN;
    }

    /**
     * Sets the value of the subjectDN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectDN(String value) {
        this.subjectDN = value;
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
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

}

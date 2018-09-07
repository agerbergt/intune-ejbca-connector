
package org.ejbca.core.protocol.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for revokeStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="revokeStatus"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="certificateSN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="issuerDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="revocationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "revokeStatus", propOrder = {
    "certificateSN",
    "issuerDN",
    "reason",
    "revocationDate"
})
public class RevokeStatus {

    protected String certificateSN;
    protected String issuerDN;
    protected int reason;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar revocationDate;

    /**
     * Gets the value of the certificateSN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateSN() {
        return certificateSN;
    }

    /**
     * Sets the value of the certificateSN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateSN(String value) {
        this.certificateSN = value;
    }

    /**
     * Gets the value of the issuerDN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuerDN() {
        return issuerDN;
    }

    /**
     * Sets the value of the issuerDN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuerDN(String value) {
        this.issuerDN = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     */
    public int getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     */
    public void setReason(int value) {
        this.reason = value;
    }

    /**
     * Gets the value of the revocationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRevocationDate() {
        return revocationDate;
    }

    /**
     * Sets the value of the revocationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRevocationDate(XMLGregorianCalendar value) {
        this.revocationDate = value;
    }

}

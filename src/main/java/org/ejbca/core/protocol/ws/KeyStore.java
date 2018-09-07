
package org.ejbca.core.protocol.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for keyStore complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="keyStore"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.protocol.core.ejbca.org/}tokenCertificateResponseWS"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="keystoreData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "keyStore", propOrder = {
    "keystoreData"
})
public class KeyStore
    extends TokenCertificateResponseWS
{

    protected byte[] keystoreData;

    /**
     * Gets the value of the keystoreData property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getKeystoreData() {
        return keystoreData;
    }

    /**
     * Sets the value of the keystoreData property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setKeystoreData(byte[] value) {
        this.keystoreData = value;
    }

}

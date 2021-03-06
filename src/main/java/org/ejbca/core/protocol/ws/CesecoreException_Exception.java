
package org.ejbca.core.protocol.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.3
 * 2018-09-27T08:23:55.998+02:00
 * Generated source version: 3.1.3
 */

@WebFault(name = "CesecoreException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class CesecoreException_Exception extends Exception {
    
    private org.ejbca.core.protocol.ws.CesecoreException cesecoreException;

    public CesecoreException_Exception() {
        super();
    }
    
    public CesecoreException_Exception(String message) {
        super(message);
    }
    
    public CesecoreException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public CesecoreException_Exception(String message, org.ejbca.core.protocol.ws.CesecoreException cesecoreException) {
        super(message);
        this.cesecoreException = cesecoreException;
    }

    public CesecoreException_Exception(String message, org.ejbca.core.protocol.ws.CesecoreException cesecoreException, Throwable cause) {
        super(message, cause);
        this.cesecoreException = cesecoreException;
    }

    public org.ejbca.core.protocol.ws.CesecoreException getFaultInfo() {
        return this.cesecoreException;
    }
}

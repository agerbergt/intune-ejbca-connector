
package org.ejbca.core.protocol.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.3
 * 2018-08-06T10:49:50.899+02:00
 * Generated source version: 3.1.3
 */

@WebFault(name = "CADoesntExistsException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class CADoesntExistsException_Exception extends Exception {
    
    private org.ejbca.core.protocol.ws.CADoesntExistsException caDoesntExistsException;

    public CADoesntExistsException_Exception() {
        super();
    }
    
    public CADoesntExistsException_Exception(String message) {
        super(message);
    }
    
    public CADoesntExistsException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public CADoesntExistsException_Exception(String message, org.ejbca.core.protocol.ws.CADoesntExistsException caDoesntExistsException) {
        super(message);
        this.caDoesntExistsException = caDoesntExistsException;
    }

    public CADoesntExistsException_Exception(String message, org.ejbca.core.protocol.ws.CADoesntExistsException caDoesntExistsException, Throwable cause) {
        super(message, cause);
        this.caDoesntExistsException = caDoesntExistsException;
    }

    public org.ejbca.core.protocol.ws.CADoesntExistsException getFaultInfo() {
        return this.caDoesntExistsException;
    }
}
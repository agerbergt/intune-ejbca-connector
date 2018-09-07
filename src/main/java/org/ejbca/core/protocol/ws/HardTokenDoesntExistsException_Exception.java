
package org.ejbca.core.protocol.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.3
 * 2018-08-06T10:49:50.892+02:00
 * Generated source version: 3.1.3
 */

@WebFault(name = "HardTokenDoesntExistsException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class HardTokenDoesntExistsException_Exception extends Exception {
    
    private org.ejbca.core.protocol.ws.HardTokenDoesntExistsException hardTokenDoesntExistsException;

    public HardTokenDoesntExistsException_Exception() {
        super();
    }
    
    public HardTokenDoesntExistsException_Exception(String message) {
        super(message);
    }
    
    public HardTokenDoesntExistsException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public HardTokenDoesntExistsException_Exception(String message, org.ejbca.core.protocol.ws.HardTokenDoesntExistsException hardTokenDoesntExistsException) {
        super(message);
        this.hardTokenDoesntExistsException = hardTokenDoesntExistsException;
    }

    public HardTokenDoesntExistsException_Exception(String message, org.ejbca.core.protocol.ws.HardTokenDoesntExistsException hardTokenDoesntExistsException, Throwable cause) {
        super(message, cause);
        this.hardTokenDoesntExistsException = hardTokenDoesntExistsException;
    }

    public org.ejbca.core.protocol.ws.HardTokenDoesntExistsException getFaultInfo() {
        return this.hardTokenDoesntExistsException;
    }
}
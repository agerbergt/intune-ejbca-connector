
package org.ejbca.core.protocol.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.3
 * 2018-08-06T10:49:50.938+02:00
 * Generated source version: 3.1.3
 */

@WebFault(name = "SignRequestException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class SignRequestException_Exception extends Exception {
    
    private org.ejbca.core.protocol.ws.SignRequestException signRequestException;

    public SignRequestException_Exception() {
        super();
    }
    
    public SignRequestException_Exception(String message) {
        super(message);
    }
    
    public SignRequestException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public SignRequestException_Exception(String message, org.ejbca.core.protocol.ws.SignRequestException signRequestException) {
        super(message);
        this.signRequestException = signRequestException;
    }

    public SignRequestException_Exception(String message, org.ejbca.core.protocol.ws.SignRequestException signRequestException, Throwable cause) {
        super(message, cause);
        this.signRequestException = signRequestException;
    }

    public org.ejbca.core.protocol.ws.SignRequestException getFaultInfo() {
        return this.signRequestException;
    }
}
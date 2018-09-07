
package org.ejbca.core.protocol.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.3
 * 2018-08-06T10:49:50.928+02:00
 * Generated source version: 3.1.3
 */

@WebFault(name = "AlreadyRevokedException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class AlreadyRevokedException_Exception extends Exception {
    
    private org.ejbca.core.protocol.ws.AlreadyRevokedException alreadyRevokedException;

    public AlreadyRevokedException_Exception() {
        super();
    }
    
    public AlreadyRevokedException_Exception(String message) {
        super(message);
    }
    
    public AlreadyRevokedException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyRevokedException_Exception(String message, org.ejbca.core.protocol.ws.AlreadyRevokedException alreadyRevokedException) {
        super(message);
        this.alreadyRevokedException = alreadyRevokedException;
    }

    public AlreadyRevokedException_Exception(String message, org.ejbca.core.protocol.ws.AlreadyRevokedException alreadyRevokedException, Throwable cause) {
        super(message, cause);
        this.alreadyRevokedException = alreadyRevokedException;
    }

    public org.ejbca.core.protocol.ws.AlreadyRevokedException getFaultInfo() {
        return this.alreadyRevokedException;
    }
}
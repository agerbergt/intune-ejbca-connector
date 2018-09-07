
package org.ejbca.core.protocol.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.3
 * 2018-08-06T10:49:50.901+02:00
 * Generated source version: 3.1.3
 */

@WebFault(name = "AuthorizationDeniedException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class AuthorizationDeniedException_Exception extends Exception {
    
    private org.ejbca.core.protocol.ws.AuthorizationDeniedException authorizationDeniedException;

    public AuthorizationDeniedException_Exception() {
        super();
    }
    
    public AuthorizationDeniedException_Exception(String message) {
        super(message);
    }
    
    public AuthorizationDeniedException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationDeniedException_Exception(String message, org.ejbca.core.protocol.ws.AuthorizationDeniedException authorizationDeniedException) {
        super(message);
        this.authorizationDeniedException = authorizationDeniedException;
    }

    public AuthorizationDeniedException_Exception(String message, org.ejbca.core.protocol.ws.AuthorizationDeniedException authorizationDeniedException, Throwable cause) {
        super(message, cause);
        this.authorizationDeniedException = authorizationDeniedException;
    }

    public org.ejbca.core.protocol.ws.AuthorizationDeniedException getFaultInfo() {
        return this.authorizationDeniedException;
    }
}
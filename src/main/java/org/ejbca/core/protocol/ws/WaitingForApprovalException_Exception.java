
package org.ejbca.core.protocol.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.3
 * 2018-09-27T08:23:55.993+02:00
 * Generated source version: 3.1.3
 */

@WebFault(name = "WaitingForApprovalException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class WaitingForApprovalException_Exception extends Exception {
    
    private org.ejbca.core.protocol.ws.WaitingForApprovalException waitingForApprovalException;

    public WaitingForApprovalException_Exception() {
        super();
    }
    
    public WaitingForApprovalException_Exception(String message) {
        super(message);
    }
    
    public WaitingForApprovalException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public WaitingForApprovalException_Exception(String message, org.ejbca.core.protocol.ws.WaitingForApprovalException waitingForApprovalException) {
        super(message);
        this.waitingForApprovalException = waitingForApprovalException;
    }

    public WaitingForApprovalException_Exception(String message, org.ejbca.core.protocol.ws.WaitingForApprovalException waitingForApprovalException, Throwable cause) {
        super(message, cause);
        this.waitingForApprovalException = waitingForApprovalException;
    }

    public org.ejbca.core.protocol.ws.WaitingForApprovalException getFaultInfo() {
        return this.waitingForApprovalException;
    }
}

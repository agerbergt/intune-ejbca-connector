
package org.ejbca.core.protocol.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.3
 * 2018-08-06T10:49:50.926+02:00
 * Generated source version: 3.1.3
 */

@WebFault(name = "ApprovalException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class ApprovalException_Exception extends Exception {
    
    private org.ejbca.core.protocol.ws.ApprovalException approvalException;

    public ApprovalException_Exception() {
        super();
    }
    
    public ApprovalException_Exception(String message) {
        super(message);
    }
    
    public ApprovalException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public ApprovalException_Exception(String message, org.ejbca.core.protocol.ws.ApprovalException approvalException) {
        super(message);
        this.approvalException = approvalException;
    }

    public ApprovalException_Exception(String message, org.ejbca.core.protocol.ws.ApprovalException approvalException, Throwable cause) {
        super(message, cause);
        this.approvalException = approvalException;
    }

    public org.ejbca.core.protocol.ws.ApprovalException getFaultInfo() {
        return this.approvalException;
    }
}
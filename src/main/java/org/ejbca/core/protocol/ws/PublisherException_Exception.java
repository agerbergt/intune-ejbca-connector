
package org.ejbca.core.protocol.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.3
 * 2018-09-27T08:23:56.031+02:00
 * Generated source version: 3.1.3
 */

@WebFault(name = "PublisherException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class PublisherException_Exception extends Exception {
    
    private org.ejbca.core.protocol.ws.PublisherException publisherException;

    public PublisherException_Exception() {
        super();
    }
    
    public PublisherException_Exception(String message) {
        super(message);
    }
    
    public PublisherException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public PublisherException_Exception(String message, org.ejbca.core.protocol.ws.PublisherException publisherException) {
        super(message);
        this.publisherException = publisherException;
    }

    public PublisherException_Exception(String message, org.ejbca.core.protocol.ws.PublisherException publisherException, Throwable cause) {
        super(message, cause);
        this.publisherException = publisherException;
    }

    public org.ejbca.core.protocol.ws.PublisherException getFaultInfo() {
        return this.publisherException;
    }
}

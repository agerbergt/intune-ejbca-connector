
package org.ejbca.core.protocol.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.3
 * 2018-09-27T08:23:56.033+02:00
 * Generated source version: 3.1.3
 */

@WebFault(name = "CryptoTokenOfflineException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class CryptoTokenOfflineException_Exception extends Exception {
    
    private org.ejbca.core.protocol.ws.CryptoTokenOfflineException cryptoTokenOfflineException;

    public CryptoTokenOfflineException_Exception() {
        super();
    }
    
    public CryptoTokenOfflineException_Exception(String message) {
        super(message);
    }
    
    public CryptoTokenOfflineException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoTokenOfflineException_Exception(String message, org.ejbca.core.protocol.ws.CryptoTokenOfflineException cryptoTokenOfflineException) {
        super(message);
        this.cryptoTokenOfflineException = cryptoTokenOfflineException;
    }

    public CryptoTokenOfflineException_Exception(String message, org.ejbca.core.protocol.ws.CryptoTokenOfflineException cryptoTokenOfflineException, Throwable cause) {
        super(message, cause);
        this.cryptoTokenOfflineException = cryptoTokenOfflineException;
    }

    public org.ejbca.core.protocol.ws.CryptoTokenOfflineException getFaultInfo() {
        return this.cryptoTokenOfflineException;
    }
}

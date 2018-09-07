package org.certificateservices.intune.ejbcaconnector

/**
 * Exception that indicates an invalid or missing configuration which
 * prevented the application to be executed as expected.
 *
 */
class InvalidConfigurationException extends Exception {
    /**
     * Exception that indicates an invalid or missing configuration.
     * @param message description of the exception.
     */
    InvalidConfigurationException(String message){
        super(message);
    }

    /**
     * Exception that indicates an invalid or missing configuration.
     * @param message description of the exception.
     * @param cause optional cause of the exception.
     */
    InvalidConfigurationException(String message, Throwable cause){
        super(message,cause);
    }
}

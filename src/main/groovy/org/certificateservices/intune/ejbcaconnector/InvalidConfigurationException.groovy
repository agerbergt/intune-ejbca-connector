/**
 * Copyright (C) 2018 CGI Certificate Services
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.certificateservices.intune.ejbcaconnector

/**
 * Exception that indicates an invalid or missing configuration which
 * prevented the application to be executed as expected.
 *
 * @author Tobias Agerberg
 */
class InvalidConfigurationException extends Exception {
    /**
     * Exception that indicates an invalid or missing configuration.
     * @param message description of the exception.
     */
    InvalidConfigurationException(String message){
        super(message)
    }

    /**
     * Exception that indicates an invalid or missing configuration.
     * @param message description of the exception.
     * @param cause optional cause of the exception.
     */
    InvalidConfigurationException(String message, Throwable cause){
        super(message,cause)
    }
}

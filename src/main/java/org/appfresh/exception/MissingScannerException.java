package org.appfresh.exception;

/**
 * Exception for invalid configuration or missing configuration file
 */
public class MissingScannerException extends RuntimeException {

    public MissingScannerException(String message) {
        super(message);
    }

}

package org.appfresh.exception;

/**
 * Exception for invalid instance initialization or invalid configuration of instance
 */
public class InvalidInstanceException extends RuntimeException {

    public InvalidInstanceException(String message) {
        super(message);
    }

    public InvalidInstanceException(String message, Throwable cause) {
        super(message, cause);
    }

}

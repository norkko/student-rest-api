
package com.api.controllers.exceptions;

public class InternalErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InternalErrorException() {}

    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}

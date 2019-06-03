package com.api.controllers.exceptions;

public class MethodNotAllowedException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public MethodNotAllowedException() {}

    public MethodNotAllowedException(String message) {
        super(message);
    }

    public MethodNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

}

package com.api.controllers;

import com.api.controllers.exceptions.*;
import com.api.models.ExceptionResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    /**
     * 404 Not Found
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> notFoundException(NotFoundException exception) {
        ExceptionResponseModel response = new ExceptionResponseModel(exception.getMessage(),404);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 405 Method Not Allowed
     */
    @ExceptionHandler(value = MethodNotAllowedException.class)
    public ResponseEntity<Object> methodNotAllowedException(MethodNotAllowedException exception) {
        ExceptionResponseModel response = new ExceptionResponseModel(exception.getMessage(),405);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 400 Bad Request
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> badRequestException(BadRequestException exception) {
        ExceptionResponseModel response = new ExceptionResponseModel(exception.getMessage(),400);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 400 Bad Request(FileStorageException)
     */
    @ExceptionHandler(value = FileStorageException.class)
    public ResponseEntity<Object> fileStorageException(FileStorageException exception) {
        ExceptionResponseModel response = new ExceptionResponseModel(exception.getMessage(),400);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    /**
     * 500 Internal Error
     */
    @ExceptionHandler(value = InternalErrorException.class)
    public ResponseEntity<Object> internalErrorException(FileStorageException exception) {
        ExceptionResponseModel response = new ExceptionResponseModel(exception.getMessage(),500);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

package com.api.models;

public class ExceptionResponseModel {

    private String error;
    private int errorCode;

    public ExceptionResponseModel() {

    }

    public ExceptionResponseModel(String error, int errorCode) {
        this.error = error;
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

package com.MediConnect.App.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}

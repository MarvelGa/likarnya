package com.epam.likarnya.exception;

public class EncryptException extends RuntimeException{
    public EncryptException() {
    }

    public EncryptException(String message) {
        super(message);
    }

    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }
}

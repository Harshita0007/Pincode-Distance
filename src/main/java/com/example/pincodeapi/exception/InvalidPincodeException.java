package com.example.pincodeapi.exception;

public class InvalidPincodeException extends RuntimeException {
    public InvalidPincodeException(String message) {
        super(message);
    }
}

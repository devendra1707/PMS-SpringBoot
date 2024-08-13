package com.pms.exception;

public class InsufficientProductQuantityException extends RuntimeException {
    public InsufficientProductQuantityException(String message) {
        super(message);
    }
}


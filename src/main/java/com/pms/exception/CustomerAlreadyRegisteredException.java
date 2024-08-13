package com.pms.exception;

public class CustomerAlreadyRegisteredException extends RuntimeException {
    public CustomerAlreadyRegisteredException(String message) {
        super(message);
    }
}

package com.pms.exception;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException() {
		super("User with this Username not found in database !! ...");
	}

	public ResourceNotFoundException(String msg) {
		super(msg);
	}
}

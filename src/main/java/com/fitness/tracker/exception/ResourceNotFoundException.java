package com.fitness.tracker.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5356858430468793444L;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}

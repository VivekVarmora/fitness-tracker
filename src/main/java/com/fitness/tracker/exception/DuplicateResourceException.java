package com.fitness.tracker.exception;

public class DuplicateResourceException extends RuntimeException {

	private static final long serialVersionUID = 5356858430462293444L;

	public DuplicateResourceException() {
	}

	public DuplicateResourceException(String message) {
		super(message);
	}

}

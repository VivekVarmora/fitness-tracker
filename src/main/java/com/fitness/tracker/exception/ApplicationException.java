package com.fitness.tracker.exception;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 5356858430468793702L;

	public ApplicationException() {
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

}

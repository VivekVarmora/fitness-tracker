package com.fitness.tracker.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<Object> handleDuplicateUser(DuplicateResourceException ex, WebRequest request) {
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false));
	}

	private ResponseEntity<Object> buildResponse(HttpStatus status, String message, String path) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("path", path.replace("uri=", ""));
		return new ResponseEntity<>(body, status);
	}

}
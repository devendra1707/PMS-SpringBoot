package com.pms.exception;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getConstraintViolations().forEach(violation -> {
			String fieldName = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
			String errorMessage = violation.getMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CustomerNotFound.class)
	public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFound ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

//	@ExceptionHandler(CustomerAlreadyRegisteredException.class)
//	public ResponseEntity<String> handleCustomerAlreadyRegisteredException(CustomerAlreadyRegisteredException ex) {
//		return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
//	}
	@ExceptionHandler(CustomerAlreadyRegisteredException.class)
	public ResponseEntity<Map<String, String>> handleCustomerAlreadyRegisteredException(
			CustomerAlreadyRegisteredException ex) {
		Map<String, String> response = new HashMap<>();
		response.put("message", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
}

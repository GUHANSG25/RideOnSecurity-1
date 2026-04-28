package com.rideon.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFound(ResourceNotFoundException ex){
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND.value(),ex.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorDetails> handleAuthentication(AuthenticationException ex){
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_ACCEPTABLE.value(),ex.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorDetails> handleValidation(ValidationException ex){
        ErrorDetails error = new ErrorDetails(HttpStatus.BAD_REQUEST.value(),ex.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
	
	 @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

	        Map<String, String> errors = new HashMap<>();

	        ex.getBindingResult().getFieldErrors()
	                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

	        return ResponseEntity.badRequest().body(errors);
	    }
}
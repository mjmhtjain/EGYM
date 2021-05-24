package com.egym.recruiting.codingtask.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;

@ControllerAdvice
@RestController
public class ErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public final ResponseEntity<Void> handleIllegalArgumentException(final IllegalArgumentException ex,
			final WebRequest request) {
		return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(InvalidDefinitionException.class)
	public final ResponseEntity<Void> handleInvalidDefinitionException(final InvalidDefinitionException ex,
			final WebRequest request) {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
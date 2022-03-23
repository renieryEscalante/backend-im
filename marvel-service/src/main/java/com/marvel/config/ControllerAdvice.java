package com.marvel.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.marvel.common.CustomException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {
	
    @ExceptionHandler(value = { CustomException.class })
	protected ResponseEntity<Object> handleConflict(CustomException ex, WebRequest request) {
		return handleExceptionInternal(ex, ex, new HttpHeaders(), ex.getHttpStatus(), request);
	}
    
}

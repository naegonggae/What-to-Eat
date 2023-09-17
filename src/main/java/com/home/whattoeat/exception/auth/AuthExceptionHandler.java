package com.home.whattoeat.exception.auth;

import com.home.whattoeat.exception.ErrorResponse;
import com.home.whattoeat.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {
	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<?> passwordMismatchException(PasswordMismatchException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}
}

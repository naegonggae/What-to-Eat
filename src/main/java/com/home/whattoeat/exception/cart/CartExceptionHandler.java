package com.home.whattoeat.exception.cart;

import com.home.whattoeat.dto.Response;
import com.home.whattoeat.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CartExceptionHandler {

	@ExceptionHandler(NoSuchCartException.class)
	public ResponseEntity<?> noSuchCartException(NoSuchCartException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}
}

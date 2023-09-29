package com.home.whattoeat.exception.review;

import com.home.whattoeat.dto.Response;
import com.home.whattoeat.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReviewExceptionHandler {

	@ExceptionHandler(NoSuchReviewException.class)
	public ResponseEntity<?> noSuchReviewException(NoSuchReviewException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}
}

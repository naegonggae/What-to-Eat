package com.home.whattoeat.exception.reataurant;

import com.home.whattoeat.exception.ErrorResponse;
import com.home.whattoeat.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestaurantExceptionHandler {

	@ExceptionHandler(NoSuchRestaurantException.class)
	public ResponseEntity<?> noSuchRestaurantException(NoSuchRestaurantException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

	@ExceptionHandler(NoSuchKeywordException.class)
	public ResponseEntity<?> noSuchKeywordException(NoSuchKeywordException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

}

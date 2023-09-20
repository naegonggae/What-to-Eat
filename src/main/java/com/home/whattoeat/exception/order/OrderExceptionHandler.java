package com.home.whattoeat.exception.order;

import com.home.whattoeat.dto.Response;
import com.home.whattoeat.exception.ErrorResponse;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrderExceptionHandler {

	@ExceptionHandler(NoSuchOrderException.class)
	public ResponseEntity<?> noSuchOrderException(NoSuchOrderException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

}

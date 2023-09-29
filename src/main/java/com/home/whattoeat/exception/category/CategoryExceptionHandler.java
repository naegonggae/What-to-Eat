package com.home.whattoeat.exception.category;

import com.home.whattoeat.dto.Response;
import com.home.whattoeat.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CategoryExceptionHandler {

	@ExceptionHandler(NoSuchCategoryException.class)
	public ResponseEntity<?> noSuchCategoryException(NoSuchCategoryException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

	@ExceptionHandler(DuplicateCategoryException.class)
	public ResponseEntity<?> duplicateCategoryException(DuplicateCategoryException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

}

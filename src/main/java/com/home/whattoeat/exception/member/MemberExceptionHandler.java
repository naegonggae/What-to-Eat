package com.home.whattoeat.exception.member;

import com.home.whattoeat.exception.ErrorResponse;
import com.home.whattoeat.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MemberExceptionHandler {

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<?> duplicateEmailException(DuplicateEmailException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

	@ExceptionHandler(DuplicateUsernameException.class)
	public ResponseEntity<?> duplicateUsernameException(DuplicateUsernameException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}

	@ExceptionHandler(NoSuchMemberException.class)
	public ResponseEntity<?> noSuchMemberException(NoSuchMemberException e) {
		return ResponseEntity.status(e.getStatus())
				.body(Response.error(new ErrorResponse(e.getStatus(), e.getStatus().value(), e.getMessage())));
	}


}

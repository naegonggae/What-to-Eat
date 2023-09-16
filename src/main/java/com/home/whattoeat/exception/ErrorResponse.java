package com.home.whattoeat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {

	public HttpStatus httpStatus;
	public int stateType;
	public String message;
}

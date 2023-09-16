package com.home.whattoeat.exception.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PasswordMismatchException extends RuntimeException {

	private final String message = "비밀번호가 일치하지 않습니다.";
	private final HttpStatus status = HttpStatus.BAD_REQUEST;
}

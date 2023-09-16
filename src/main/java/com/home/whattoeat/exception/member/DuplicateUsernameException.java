package com.home.whattoeat.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateUsernameException extends RuntimeException {

	private final String message = "이미 존재하는 아이디입니다.";
	private final HttpStatus status = HttpStatus.BAD_REQUEST;
}

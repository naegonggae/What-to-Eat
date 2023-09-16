package com.home.whattoeat.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateEmailException extends RuntimeException {

	private final String message = "이미 사용중인 이메일입니다.";
	private final HttpStatus status = HttpStatus.BAD_REQUEST;
}

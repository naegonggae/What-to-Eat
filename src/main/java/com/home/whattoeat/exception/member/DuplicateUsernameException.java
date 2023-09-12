package com.home.whattoeat.exception.member;

import org.springframework.http.HttpStatus;

public class DuplicateUsernameException extends RuntimeException {

	private static final String MESSAGE = "이미 사용중인 이름입니다.";

	public DuplicateUsernameException() {
		super(MESSAGE);
	}

}

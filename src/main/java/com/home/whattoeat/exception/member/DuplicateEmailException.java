package com.home.whattoeat.exception.member;

public class DuplicateEmailException extends RuntimeException {

	private static final String MASSAGE = "이미 사용중인 이메일입니다.";

	public DuplicateEmailException() {
		super(MASSAGE);
	}

}

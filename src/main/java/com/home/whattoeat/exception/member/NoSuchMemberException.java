package com.home.whattoeat.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchMemberException extends RuntimeException {

	private final String message = "존재하지 않는 회원입니다.";
	private final HttpStatus status = HttpStatus.NOT_FOUND;
}

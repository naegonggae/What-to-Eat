package com.home.whattoeat.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccessDeniedException extends RuntimeException {

	private String message = "접근 권한이 없는 요청입니다.";
	private final HttpStatus status = HttpStatus.UNAUTHORIZED;

}

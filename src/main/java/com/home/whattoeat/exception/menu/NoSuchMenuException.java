package com.home.whattoeat.exception.menu;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchMenuException extends RuntimeException {

	private final String message = "존재하지 않는 메뉴입니다.";
	private final HttpStatus status = HttpStatus.NOT_FOUND;

}

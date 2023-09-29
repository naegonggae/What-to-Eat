package com.home.whattoeat.exception.cart;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchCartException extends RuntimeException {

	private final String message = "카트가 존재하지 않습니다. 새로운 카트를 생성해주세요.";
	private final HttpStatus status = HttpStatus.NOT_FOUND;

}

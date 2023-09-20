package com.home.whattoeat.exception.order;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchOrderException extends RuntimeException{

	private final String message = "존재하지 않는 주문입니다.";
	private final HttpStatus status = HttpStatus.NOT_FOUND;

}

package com.home.whattoeat.exception.category;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchCategoryException extends RuntimeException {

	private final String message = "존재하지 않는 카테고리입니다.";
	private final HttpStatus status = HttpStatus.NOT_FOUND;

}

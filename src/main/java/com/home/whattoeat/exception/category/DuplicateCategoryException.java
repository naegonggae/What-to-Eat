package com.home.whattoeat.exception.category;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateCategoryException extends RuntimeException {

	private final String message = "이미 저장된 카테고리입니다.";
	private final HttpStatus status = HttpStatus.BAD_REQUEST;

}

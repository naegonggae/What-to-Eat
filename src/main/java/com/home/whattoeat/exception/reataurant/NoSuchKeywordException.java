package com.home.whattoeat.exception.reataurant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchKeywordException extends RuntimeException {

	private final String message = "검색어를 입력해주세요.";
	private final HttpStatus status = HttpStatus.NOT_FOUND;

}

package com.home.whattoeat.exception.review;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchReviewException extends RuntimeException {

	private final String message = "존재하지 않는 리뷰입니다.";
	private final HttpStatus status = HttpStatus.NOT_FOUND;

}

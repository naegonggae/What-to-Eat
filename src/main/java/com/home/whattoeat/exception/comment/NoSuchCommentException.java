package com.home.whattoeat.exception.comment;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchCommentException extends RuntimeException {

	private final String message = "존재하지 않는 댓글입니다.";
	private final HttpStatus status = HttpStatus.NOT_FOUND;

}

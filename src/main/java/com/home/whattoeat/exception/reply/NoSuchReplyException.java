package com.home.whattoeat.exception.reply;

import jakarta.servlet.http.HttpServlet;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchReplyException extends RuntimeException {

	private final String message = "존재하지 않는 대댓글입니다.";
	private final HttpStatus status = HttpStatus.NOT_FOUND;

}

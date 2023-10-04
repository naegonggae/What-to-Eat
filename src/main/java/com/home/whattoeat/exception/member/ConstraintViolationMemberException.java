package com.home.whattoeat.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ConstraintViolationMemberException extends
		RuntimeException {

	private final String message = "회원탈퇴 이전에 보유하신 식당을 삭제해주세요.";
	private final HttpStatus status = HttpStatus.BAD_REQUEST;

}

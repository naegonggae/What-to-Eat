package com.home.whattoeat.exception.reataurant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateRestaurantException extends RuntimeException {

	private final String message = "이미 존재하는 식당이름입니다.";
	private final HttpStatus status = HttpStatus.BAD_REQUEST;

}

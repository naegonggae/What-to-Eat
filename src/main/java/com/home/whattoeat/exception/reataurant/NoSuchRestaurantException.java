package com.home.whattoeat.exception.reataurant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoSuchRestaurantException extends RuntimeException {

	private final String message = "존재하지 않는 음식점입니다.";
	private final HttpStatus status = HttpStatus.NOT_FOUND;
}

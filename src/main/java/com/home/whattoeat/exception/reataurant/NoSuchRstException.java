package com.home.whattoeat.exception.reataurant;

public class NoSuchRstException extends RuntimeException {

	private static final String MESSAGE = "존재하지 않는 음식점입니다.";

	public NoSuchRstException() {
		super(MESSAGE);
	}

}

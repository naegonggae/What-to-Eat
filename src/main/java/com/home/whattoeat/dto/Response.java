package com.home.whattoeat.dto;

import com.home.whattoeat.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
	private String resultCode;
	private T result;

	public static Response<ErrorResponse> error(ErrorResponse errorResponse){
		return new Response<>("ERROR", errorResponse);
	}
	public static <T> Response<T> success(T result){
		return new Response<>("SUCCESS", result);
	}
}

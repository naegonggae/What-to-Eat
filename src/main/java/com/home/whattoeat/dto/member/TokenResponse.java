package com.home.whattoeat.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {

	private String accessToken;

	public static TokenResponse form(String token) {
		return new TokenResponse(token);
	}

}

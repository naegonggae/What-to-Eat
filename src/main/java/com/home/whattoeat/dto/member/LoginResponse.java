package com.home.whattoeat.dto.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {

	private String Message;

	public static LoginResponse from() {
		return new LoginResponse("로그인 되었습니다.");
	}
}

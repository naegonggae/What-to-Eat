package com.home.whattoeat.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSaveRequest {

	@NotEmpty(message = "회원 이름은 필수 입니다.")
	private String username;
	private String email;
	@NotEmpty(message = "비밀번호는 필수 입니다.")
	private String password;
	// 010-0000-0000 형식으로 받아야함
	private String phoneNumber;

}

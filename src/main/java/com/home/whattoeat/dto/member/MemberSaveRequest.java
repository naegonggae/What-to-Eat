package com.home.whattoeat.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSaveRequest {

	@NotEmpty(message = "회원 이름은 필수 입니다.")
	private String username;
	private String email;
	@NotEmpty(message = "비밀번호는 필수 입니다.")
	private String password;

}

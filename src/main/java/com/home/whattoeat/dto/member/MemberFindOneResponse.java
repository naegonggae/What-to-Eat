package com.home.whattoeat.dto.member;

import com.home.whattoeat.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberFindOneResponse {

	private Long id;
	private String username;
	private String email;
	private String password;

	public static MemberFindOneResponse from(Member member) {
		return new MemberFindOneResponse(
				member.getId(),
				member.getUsername(),
				member.getEmail(),
				member.getPassword());
	}

}

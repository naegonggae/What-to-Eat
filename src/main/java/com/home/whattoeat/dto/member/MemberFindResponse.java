package com.home.whattoeat.dto.member;

import com.home.whattoeat.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberFindResponse {

	private Long id;
	private String username;
	private String email;
	private String phoneNumber;

	public static MemberFindResponse from(Member member) {
		return new MemberFindResponse(
				member.getId(),
				member.getUsername(),
				member.getEmail(),
				member.getPhoneNumber());
	}

}

package com.home.whattoeat.dto.member;

import com.home.whattoeat.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSaveResponse {

	private Long id;
	private String username;
	private String email;
	private String phoneNumber;

	public static MemberSaveResponse form(Member member) {
		return new MemberSaveResponse(
				member.getId(),
				member.getUsername(),
				member.getEmail(),
				member.getPhoneNumber());
	}

}

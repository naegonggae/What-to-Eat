package com.home.whattoeat.dto.member;

import com.home.whattoeat.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveResponse {

	private Long id;

	public static MemberSaveResponse form(Member member) {
		return new MemberSaveResponse(member.getId());
	}

}

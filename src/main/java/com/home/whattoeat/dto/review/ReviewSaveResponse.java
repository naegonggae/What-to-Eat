package com.home.whattoeat.dto.review;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewSaveResponse {

	private Long id;

	public static ReviewSaveResponse from(Long id) {
		return new ReviewSaveResponse(id);
	}
}

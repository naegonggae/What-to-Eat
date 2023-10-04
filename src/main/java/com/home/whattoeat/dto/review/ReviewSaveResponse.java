package com.home.whattoeat.dto.review;

import com.home.whattoeat.domain.Review;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewSaveResponse {

	private Long id;
	private String content;
	private Double starRating;

	public static ReviewSaveResponse from(Review review) {
		return new ReviewSaveResponse(
				review.getId(),
				review.getContent(),
				Math.round(review.getStarRating() * 10.0) / 10.0); // 소수점 두번째자리에서 반올림
	}
}

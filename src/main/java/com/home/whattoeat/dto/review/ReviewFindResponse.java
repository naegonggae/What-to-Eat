package com.home.whattoeat.dto.review;

import com.home.whattoeat.domain.Review;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewFindResponse {

	private Long id;
	private String content;
	private Double starRating;

	public static ReviewFindResponse from(Review review) {
		return new ReviewFindResponse(
				review.getId(),
				review.getContent(),
				Math.round(review.getStarRating() * 10.0) / 10.0);
	}
}

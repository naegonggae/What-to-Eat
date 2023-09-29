package com.home.whattoeat.dto.review;

import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewFindResponse {

	private String content;
	private Double starRating;

	public static ReviewFindResponse from(Review review) {
		return new ReviewFindResponse(review.getContent(), review.getStarRating());
	}
}

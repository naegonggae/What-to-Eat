package com.home.whattoeat.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequest {

	private String content;
	private Double starRating;

}

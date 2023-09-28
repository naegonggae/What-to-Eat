package com.home.whattoeat.dto.restuarant;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RstSearchCondition {

	private String categoryName;
	private String sortBy;
	private Double starRating; // 리뷰할때 구현
	private Integer minOrderAmount;
	private Integer maxOrderAmount;


	@QueryProjection

	public RstSearchCondition(String categoryName, String sortBy, Double starRating,
			Integer minOrderAmount, Integer maxOrderAmount) {
		this.categoryName = categoryName;
		this.sortBy = sortBy;
		this.starRating = starRating;
		this.minOrderAmount = minOrderAmount;
		this.maxOrderAmount = maxOrderAmount;
	}
}

package com.home.whattoeat.dto.restuarant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RstSearchCondition {

	private String categoryName;
	private String sortBy;
	private Double starRating;
	private Integer minOrderAmount;
	private Integer maxOrderAmount;

}

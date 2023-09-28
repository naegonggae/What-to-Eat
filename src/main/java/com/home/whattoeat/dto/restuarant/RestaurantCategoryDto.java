package com.home.whattoeat.dto.restuarant;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantCategoryDto {

	private String name;
	private String phoneNumber;

	private double starRating; // 리뷰할때 구현
	private Long numberOfOrders; // 리뷰할때 구현 지금은 하드 코딩해버리자
	private int minOrderAmount;
	private int maxOrderAmount;

	@QueryProjection // dto 가 queryDsl 에 의존한다 순수하지 못하다
	public RestaurantCategoryDto(String name, String phoneNumber, double starRating,
			Long numberOfOrders, int minOrderAmount, int maxOrderAmount) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.starRating = starRating;
		this.numberOfOrders = numberOfOrders;
		this.minOrderAmount = minOrderAmount;
		this.maxOrderAmount = maxOrderAmount;
	}

//	public static RestaurantCategoryDto of(Restaurant restaurant) {
//		return new RestaurantCategoryDto(
//				restaurant.getName(),
//				restaurant.getPhoneNumber(),
//				restaurant.getStarRating(),
//				restaurant.getNumberOfOrders(),
//				restaurant.getMinOrderAmount(),
//				restaurant.getMaxOrderAmount()
//		);
//	}

}

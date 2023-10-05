package com.home.whattoeat.dto.restuarant;

import com.home.whattoeat.domain.Address;
import com.home.whattoeat.domain.Restaurant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RstFindResponse {

	private Long id;
	private String name;
	private String phoneNumber;
	private String description;
	private Address address;
	private Double starRating;
	private Long numberOfOrders;
	private Integer minOrderAmount;
	private Integer maxOrderAmount;
	private Integer reviewCount;

	public static RstFindResponse from(Restaurant restaurant) {
		return new RstFindResponse(
				restaurant.getId(),
				restaurant.getName(),
				restaurant.getPhoneNumber(),
				restaurant.getDescription(),
				restaurant.getAddress(),
				Math.round(restaurant.getStarRating()*10.0) / 10.0,
				restaurant.getNumberOfOrders(),
				restaurant.getMinOrderAmount(),
				restaurant.getMaxOrderAmount(),
				restaurant.getReviewCount());
	}

}

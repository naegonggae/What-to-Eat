package com.home.whattoeat.dto.restuarant;

import com.home.whattoeat.domain.Address;
import com.home.whattoeat.domain.Restaurant;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RstSaveResponse {

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

	public static RstSaveResponse from(Restaurant restaurant) {
		return new RstSaveResponse(
				restaurant.getId(),
				restaurant.getName(),
				restaurant.getPhoneNumber(),
				restaurant.getDescription(),
				restaurant.getAddress(),
				restaurant.getStarRating(),
				restaurant.getNumberOfOrders(),
				restaurant.getMinOrderAmount(),
				restaurant.getMaxOrderAmount(),
				restaurant.getReviewCount());
	}

}

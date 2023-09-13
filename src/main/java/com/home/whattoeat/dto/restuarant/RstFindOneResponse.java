package com.home.whattoeat.dto.restuarant;

import com.home.whattoeat.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RstFindOneResponse {

	private String name;
	private String phoneNumber;
	private String cuisineType;

	public static RstFindOneResponse from(Restaurant restaurant) {
		return new RstFindOneResponse(restaurant.getName(), restaurant.getPhoneNumber(), restaurant.getCuisineType());
	}

}

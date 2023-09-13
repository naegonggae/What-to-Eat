package com.home.whattoeat.dto.restuarant;

import com.home.whattoeat.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RstFindAllResponse {

	private String name;
	private String phoneNumber;
	private String cuisineType;

	public static RstFindAllResponse from(Restaurant restaurant) {
		return new RstFindAllResponse(restaurant.getName(), restaurant.getPhoneNumber(),
				restaurant.getCuisineType());
	}

}

package com.home.whattoeat.dto.restuarant;

import com.home.whattoeat.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RstSaveResponse {

	private Long id;

	public static RstSaveResponse from(Restaurant restaurant) {
		return new RstSaveResponse(restaurant.getId());
	}

}

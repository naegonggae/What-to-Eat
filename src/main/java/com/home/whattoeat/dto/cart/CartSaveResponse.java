package com.home.whattoeat.dto.cart;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CartSaveResponse {

	private Long id;

	public static CartSaveResponse from(Long id) {
		return new CartSaveResponse(id);
	}

}

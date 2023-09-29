package com.home.whattoeat.dto.cart;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartSaveRequest {

	private String restaurantName;
	private String menuName;
	private int quantity;

}

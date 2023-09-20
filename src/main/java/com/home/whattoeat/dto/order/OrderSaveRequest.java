package com.home.whattoeat.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSaveRequest {

	private String restaurantName;
	private String menuName;
	private int quantity;

}

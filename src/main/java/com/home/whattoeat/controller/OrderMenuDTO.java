package com.home.whattoeat.controller;

import com.home.whattoeat.domain.OrderMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMenuDTO {

	private String menuName;
	private int price;
	private String rstName;

	public static OrderMenuDTO from(OrderMenu orderMenu) {
		return new OrderMenuDTO(
				orderMenu.getMenu().getName(),
				orderMenu.getMenu().getPrice(),
				orderMenu.getMenu().getRestaurant().getName());
	}

}

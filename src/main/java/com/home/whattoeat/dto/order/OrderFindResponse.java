package com.home.whattoeat.dto.order;

import com.home.whattoeat.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFindResponse {

	private Long id;

	public static OrderFindResponse from(Order order) {
		return new OrderFindResponse(order.getId());
	}

}

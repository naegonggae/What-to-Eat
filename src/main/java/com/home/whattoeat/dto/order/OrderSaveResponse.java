package com.home.whattoeat.dto.order;

import com.home.whattoeat.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSaveResponse {

	private Long id;
//	private String name; //이게 있으면 리스폰스가 어떻게 나오려나 -> 자리 차지함

//	public static OrderSaveResponse from(Order order) {
//		return new OrderSaveResponse(order.getId());
//	}
	public OrderSaveResponse(Order order) {
		this.id = order.getId();
	}
}

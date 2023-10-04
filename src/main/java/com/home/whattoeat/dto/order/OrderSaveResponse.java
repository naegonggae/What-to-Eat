package com.home.whattoeat.dto.order;

import com.home.whattoeat.domain.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSaveResponse {

	private Long id;
	private String orderNotes;
	private Integer totalAmount;
	private String orderDate;
	private String status; // ORDER, CANCEL

	public static OrderSaveResponse from(Order order) {
		return new OrderSaveResponse(
				order.getId(),
				order.getOrderNotes(),
				order.getTotalAmount(),
				order.getOrderDate().toString(),
				order.getStatus().toString());
	}
}

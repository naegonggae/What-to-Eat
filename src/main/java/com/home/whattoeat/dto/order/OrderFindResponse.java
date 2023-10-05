package com.home.whattoeat.dto.order;

import com.home.whattoeat.domain.Order;
import com.home.whattoeat.domain.OrderMenu;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderFindResponse {

	private Long id;
	private String restaurantName;
	private Integer totalAmount;
	private Map<String, Integer> orderMenu;
	private String orderNotes;
	private String orderDate;
	private String status; // ORDER, CANCEL

	public static OrderFindResponse from(Order order) {
		Map<String, Integer> orderMenuMap = convertOrderMenuListToMap(order.getOrderMenuList());

		return new OrderFindResponse(
				order.getId(),
				order.getRestaurant().getName(), // 레스토랑 강제 초기화
//				order.getRestaurantName(), // 삭제되었을때는 이걸 쓰면 됨
				order.getTotalAmount(),
				orderMenuMap,
				order.getOrderNotes(),
				order.getOrderDate().toString(),
				order.getStatus().toString());
	}

	public static Map<String, Integer> convertOrderMenuListToMap(List<OrderMenu> orderMenuList) {
		Map<String, Integer> orderMenuMap = new HashMap<>();

		for (OrderMenu orderMenu : orderMenuList) {
			String menuName = orderMenu.getMenu().getName(); // orderMenu, menu 강제 초기화
			Integer quantity = orderMenu.getQuantity();

			// 이미 메뉴가 맵에 존재하는 경우, 수량을 더해줍니다.
			if (orderMenuMap.containsKey(menuName)) {
				Integer existingQuantity = orderMenuMap.get(menuName);
				orderMenuMap.put(menuName, existingQuantity + quantity);
			} else {
				orderMenuMap.put(menuName, quantity);
			}
		}

		return orderMenuMap;
	}

}

package com.home.whattoeat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderMenu extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "order_menu_id")
	private Long id;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;
	private int quantity;
	private int price;
	// Unit Price
	// 총가격
	// 옵션
	// 메모

	public void addOrder(Order order) {
		this.order = order;
	}

	public static OrderMenu createMenu(Menu menu, int quantity, int price) {
		// 결제?
		return OrderMenu.builder()
				.menu(menu)
				.quantity(quantity)
				.price(price)
				.build();
	}

	public OrderMenu(Menu menu, int quantity, int price) {
		this.menu = menu;
		this.quantity = quantity;
		this.price = price;
	}

	public void cancel() {
		System.out.println("재고가 없어서 딱히 할 로직이 없넹");
	}

	public int getTotalPrice() {
		return getPrice() * getQuantity();
	}
}

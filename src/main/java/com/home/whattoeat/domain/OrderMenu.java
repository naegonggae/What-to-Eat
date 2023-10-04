package com.home.whattoeat.domain;

import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class OrderMenu extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "order_menu_id")
	private Long id;
	private int quantity;
	private int price;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	// 연관관계 메서드 //
	public void addOrder(Order order) {
		this.order = order;
	}

	// 생성 메서드 //
	public static OrderMenu createMenu(CartMenu cartMenu) {
		return new OrderMenu(cartMenu);
	}
	public OrderMenu(CartMenu cartMenu) {
		this.menu = cartMenu.getMenu();
		this.quantity = cartMenu.getQuantity();
		this.price = cartMenu.getPrice();
	}

	// 비즈니스 로직 //
	public Integer getTotalPrice() {
		return getPrice() * getQuantity();
	}
}

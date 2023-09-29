package com.home.whattoeat.domain;

import com.home.whattoeat.dto.cart.CartSaveRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartMenu extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "cart_menu_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	private int quantity;
	private int price;


	public static CartMenu createCartMenu(CartSaveRequest request, Menu findMenu, Cart cart) {
		return new CartMenu(cart, findMenu, request.getQuantity(), findMenu.getPrice());
	}
	public CartMenu(Cart cart, Menu menu, int quantity, int price) {
		this.cart = cart;
		this.menu = menu;
		this.quantity = quantity;
		this.price = price;
	}
}

package com.home.whattoeat.domain;

import static lombok.AccessLevel.PROTECTED;

import com.home.whattoeat.dto.cart.CartSaveRequest;
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
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class CartMenu extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "cart_menu_id")
	private Long id;
	private Integer quantity;
	private Integer price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	// 연관관계 메서드 //
	public void addCart(Cart cart) {
		this.cart = cart;
		cart.getCartMenus().add(this);
	}

	// 생성 메서드 //
	public static CartMenu createCartMenu(CartSaveRequest request, Menu findMenu, Cart cart) {
		return new CartMenu(cart, findMenu, request.getQuantity(), findMenu.getPrice());
	}
	public CartMenu(Cart cart, Menu menu, int quantity, int price) {
		this.quantity = quantity;
		this.price = price;
		this.addCart(cart);
		this.menu = menu;
	}
}

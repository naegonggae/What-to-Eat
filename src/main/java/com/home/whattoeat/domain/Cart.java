package com.home.whattoeat.domain;

import com.home.whattoeat.dto.cart.CartSaveRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Cart extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "cart_id")
	private Long id;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE)
	private List<CartMenu> cartMenus = new ArrayList<>();

//	@OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
//	private Order order;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private int totalPrice;


	public static Cart createCart(Member member) {
		return new Cart(member, 0);
	}
	public Cart(Member member, int totalPrice) {
		this.member = member;
		this.totalPrice = totalPrice;
	}

	public void addCartMenuInMyCart(CartMenu cartMenu) {

	}

	public void clearCart() {
		cartMenus.clear();
	}
}

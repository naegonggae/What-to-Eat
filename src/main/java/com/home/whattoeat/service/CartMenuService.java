package com.home.whattoeat.service;

import com.home.whattoeat.domain.Cart;
import com.home.whattoeat.domain.CartMenu;
import com.home.whattoeat.domain.Menu;
import com.home.whattoeat.dto.cart.CartSaveRequest;
import com.home.whattoeat.repository.CartMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartMenuService {

	private final CartMenuRepository cartMenuRepository;

	// cartMenu 생성 == 장바구니에 담는 구성품(하나의 메뉴 * 수량)
	@Transactional
	public CartMenu createCartMenu(CartSaveRequest request, Menu menu, Cart cart) {
		CartMenu cartMenu = CartMenu.createCartMenu(request, menu, cart);
		CartMenu saveCartMenu = cartMenuRepository.save(cartMenu);
		return saveCartMenu;
	}
}

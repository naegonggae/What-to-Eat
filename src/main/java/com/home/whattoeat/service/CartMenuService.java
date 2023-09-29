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

	@Transactional
	public void createCartMenu(CartSaveRequest request, Menu menu, Cart cart) {
		CartMenu cartMenu = CartMenu.createCartMenu(request, menu, cart); // 메뉴랑 수량 선택
		cartMenuRepository.save(cartMenu);
	}
}

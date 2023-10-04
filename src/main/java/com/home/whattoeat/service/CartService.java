package com.home.whattoeat.service;

import com.home.whattoeat.domain.Cart;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Menu;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.dto.cart.CartSaveRequest;
import com.home.whattoeat.dto.cart.CartSaveResponse;
import com.home.whattoeat.exception.cart.NoSuchCartException;
import com.home.whattoeat.exception.menu.NoSuchMenuException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import com.home.whattoeat.repository.CartRepository;
import com.home.whattoeat.repository.MenuRepository;
import com.home.whattoeat.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepository;
	private final CartMenuService cartMenuService;
	private final MenuRepository menuRepository;
	private final RestaurantRepository restaurantRepository;

	// 장바구니 생성
	@Transactional
	public CartSaveResponse createCart(CartSaveRequest request, Member member) {

		// 장바구니가 존재하는지
		Cart findCart = cartRepository.findByMember(member).orElse(null);

		if (findCart == null) {
			// 장바구니가 없을 경우 새로운 장바구니 생성, 존재하면 사용하던거 계속 사용
			findCart = Cart.createCart(member);
		}

		// 식당이 존재하는지 체크
		Restaurant findRestaurant = restaurantRepository.findByName(request.getRestaurantName())
				.orElseThrow(NoSuchRestaurantException::new);

		// 메뉴가 존재하는지 체크
		Menu findMenu = menuRepository.findByName(request.getMenuName())
				.orElseThrow(NoSuchMenuException::new);

		// 장바구니에 CartMenu 를 하나씩 담아서 저장
		// 저장되는 cartMenu 에는 carId가 매핑되어있다.
		cartMenuService.createCartMenu(request, findMenu, findCart);

		Cart savedCart = cartRepository.save(findCart);
		return CartSaveResponse.from(savedCart.getId());
	}

	// 장바구니 비우기
	@Transactional
	public void clearCart(Long cartId) {

		Cart findCart = cartRepository.findById(cartId)
				.orElseThrow(NoSuchCartException::new);

		// 장바구니를 삭제 -> 장바구니를 참조한 cartMenu 들 삭제
		cartRepository.deleteById(findCart.getId());

	}

}

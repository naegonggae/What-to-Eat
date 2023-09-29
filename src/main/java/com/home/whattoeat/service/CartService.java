package com.home.whattoeat.service;

import com.home.whattoeat.domain.Cart;
import com.home.whattoeat.domain.CartMenu;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Menu;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.dto.cart.CartSaveRequest;
import com.home.whattoeat.dto.cart.CartSaveResponse;
import com.home.whattoeat.exception.cart.NoSuchCartException;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import com.home.whattoeat.exception.menu.NoSuchMenuException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import com.home.whattoeat.repository.CartMenuRepository;
import com.home.whattoeat.repository.CartRepository;
import com.home.whattoeat.repository.MemberRepository;
import com.home.whattoeat.repository.MenuRepository;
import com.home.whattoeat.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {
	private final CartMenuService cartMenuService;

	private final CartRepository cartRepository;
	private final RestaurantRepository restaurantRepository;
	private final MenuRepository menuRepository;
	private final MemberRepository memberRepository;
	private final CartMenuRepository cartMenuRepository;

	// 카트메뉴리스트 작성
	@Transactional
	public CartSaveResponse createCart(CartSaveRequest request, Member member) {

//		boolean isExistedCart = cartRepository.existsByMember(username);

//		Member findMember = memberRepository.findByUsername(username)
//				.orElseThrow(NoSuchMemberException::new);

//		if (!isExistedCart) Cart.createCart(findMember);
//
//		Cart findCart = cartRepository.findByMember(username)
//				.orElse(null);

		Cart findCart = cartRepository.findByMember(member).orElse(null);

		if (findCart == null) {
			// 장바구니가 없을 경우 새로운 장바구니 생성
			findCart = Cart.createCart(member);
		}

		// 내가 만든것들이 아니여도 상관없음
		Restaurant findRestaurant = restaurantRepository.findByName(request.getRestaurantName())
				.orElseThrow(NoSuchRestaurantException::new);

//		if (!member.equals(findRestaurant.getMember())) throw new AccessDeniedException();
		Menu findMenu = menuRepository.findByName(request.getMenuName())
				.orElseThrow(NoSuchMenuException::new);

		// 식당있는지
//		CartMenu cartMenu = CartMenu.createCartMenu(request, findMenu, findCart); // 메뉴랑 수량 선택


		// 내 장바구니에 넣기, 추가로 메뉴를 선택해서 넣을때마다 하나의 cart 에 집어넣어야함
//		findCart.addCartMenuInMyCart(cartMenu);
/**
 * 장바구니에 직접 넣는다 생각하지 말고 cartMenu 를 생성할때 cartID 를 부여해서 Order 에서 주문할때 id가 본인카트인걸 다 가져와서 주문을 한번에 진행
 */
//		cartMenuRepository.save(cartMenu);

		cartMenuService.createCartMenu(request, findMenu, findCart);

		Cart savedCart = cartRepository.save(findCart);
		return CartSaveResponse.from(savedCart.getId());
	}

	// 카트메뉴리스트 비우기
	@Transactional
	public void clearCart(Long cartId) {

		Cart findCart = cartRepository.findById(cartId)
				.orElseThrow(NoSuchCartException::new);

		// 장바구니를 삭제해서 장바구니를 참조한 cartMenu 들을 삭제
		cartRepository.deleteById(findCart.getId());

	}

	@Transactional
	public void clearCart2(Long cartId) {

		Cart findCart = cartRepository.findById(cartId)
				.orElseThrow(NoSuchCartException::new);

		// 이렇게 할거면 데이터 일관성을 잘 보존해야해
		findCart.clearCart();


	}

	// 장바구니 전체 조회하기
}

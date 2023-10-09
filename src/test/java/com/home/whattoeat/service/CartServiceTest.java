package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.home.whattoeat.domain.Cart;
import com.home.whattoeat.dto.cart.CartSaveRequest;
import com.home.whattoeat.dto.cart.CartSaveResponse;
import com.home.whattoeat.exception.cart.NoSuchCartException;
import com.home.whattoeat.exception.menu.NoSuchMenuException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class CartServiceTest extends ServiceTest {

	@InjectMocks
	private CartService cartService;

	@Nested
	@DisplayName("createCart 메서드는")
	class CreateCartCase {
		// given
		CartSaveRequest request = new CartSaveRequest("맥도날드", "빅맥", 10);

		@Test
		@DisplayName("cart 생성 성공")
		public void success_createCart() {
			// when
			when(cartRepository.findByMember(member)).thenReturn(Optional.ofNullable(cart));
			when(restaurantRepository.findByName(request.getRestaurantName())).thenReturn(Optional.of(restaurant));
			when(menuRepository.findByName(request.getMenuName())).thenReturn(Optional.of(menu));
			when(cartMenuService.createCartMenu(request, menu, cart)).thenReturn(cartMenu);
			when(cartRepository.save(any(Cart.class))).thenReturn(cart);

			//then
			CartSaveResponse result = cartService.createCart(request, member);

			assertThat(result.getId()).isEqualTo(1L);
		}

		@Test
		@DisplayName("존재하지 않는 음식점의 메뉴를 담을때 cart 생성 실패")
		public void fail1_createCart() {
			// when
			when(restaurantRepository.findByName(request.getRestaurantName())).thenThrow(
					NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> cartService.createCart(request, member))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 메뉴를 담을때 cart 생성 실패")
		public void fail2_createCart() {
			// when
			when(restaurantRepository.findByName(request.getRestaurantName())).thenReturn(Optional.of(restaurant));
			when(menuRepository.findByName(request.getMenuName())).thenThrow(NoSuchMenuException.class);

			// then
			assertThatThrownBy(() -> cartService.createCart(request, member))
					.isInstanceOf(NoSuchMenuException.class)
					.hasMessage("존재하지 않는 메뉴입니다.");
		}
	}

	@Nested
	@DisplayName("clearCart 메서드는")
	class ClearCartCase {

		@Test
		@DisplayName("cart 비우기 성공")
		public void success_clearCart() {
			// when
			when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

			// then
			cartService.clearCart(1L);

			verify(cartRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("존재하지 않는 장바구니를 삭제하려 할때 cart 비우기 실패")
		public void fail_clearCart() {
			// when
			when(cartRepository.findById(-1L)).thenThrow(NoSuchCartException.class);

			// then
			assertThatThrownBy(() -> cartService.clearCart(-1L))
					.isInstanceOf(NoSuchCartException.class)
					.hasMessage("카트가 존재하지 않습니다. 새로운 카트를 생성해주세요.");
		}

	}


}
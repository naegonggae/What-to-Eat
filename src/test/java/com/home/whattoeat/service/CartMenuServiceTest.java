package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.home.whattoeat.domain.CartMenu;
import com.home.whattoeat.dto.cart.CartSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class CartMenuServiceTest extends ServiceTest {

	@InjectMocks
	private CartMenuService cartMenuService;

	@Nested
	@DisplayName("createCartMenu 메서드는")
	class CreateCartMenuCase {
		// given
		CartSaveRequest request = new CartSaveRequest("맥도날드", "빅맥", 10);

		@Test
		@DisplayName("cartMenu 생성 성공")
		public void success_createCartMenu() {
			// when
			when(cartMenuRepository.save(any(CartMenu.class))).thenReturn(cartMenu2);
			
			// then
			CartMenu result = cartMenuService.createCartMenu(request, menu, cart);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getQuantity()).isEqualTo(10);
			assertThat(result.getPrice()).isEqualTo(6000);
			assertThat(result.getCart()).isEqualTo(cart);
			assertThat(result.getMenu()).isEqualTo(menu);
		}
	}

}
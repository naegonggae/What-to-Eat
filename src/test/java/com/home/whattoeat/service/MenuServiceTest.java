package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.home.whattoeat.domain.Menu;
import com.home.whattoeat.dto.menu.MenuFindResponse;
import com.home.whattoeat.dto.menu.MenuSaveRequest;
import com.home.whattoeat.dto.menu.MenuSaveResponse;
import com.home.whattoeat.dto.menu.MenuUpdateRequest;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.menu.NoSuchMenuException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class MenuServiceTest extends ServiceTest {

	@InjectMocks
	private MenuService menuService;

	@Nested
	@DisplayName("save 메서드는")
	class SaveCase {
		// given
		MenuSaveRequest request = new MenuSaveRequest("빅맥", "빅맥입니다.", 6000);

		@Test
		@DisplayName("menu 생성 성공")
		public void success_save() {

			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(menuRepository.save(any(Menu.class))).thenReturn(menu);

			// then
			MenuSaveResponse result = menuService.save(request, 1L, member);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getName()).isEqualTo("빅맥");
			assertThat(result.getDescription()).isEqualTo("빅맥입니다.");
			assertThat(result.getPrice()).isEqualTo(6000);
		}

		@Test
		@DisplayName("존재하지 않는 식당에 메뉴등록할때 menu 생성 실패")
		public void fail1_save() {

			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> menuService.save(request, -1L, member))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 등록하려는 식당을 만든사람이 일치하지 않을때 menu 생성 실패")
		public void fail2_save() {

			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

			// then
			assertThatThrownBy(() -> menuService.save(request, 1L, member2))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}

	@Nested
	@DisplayName("findOne 메서드는")
	class FindOneCase {

		@Test
		@DisplayName("menu 단건 조회 성공")
		public void success_findOne() {

			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

			// then
			MenuFindResponse result = menuService.findOne(1L, 1L);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getName()).isEqualTo("빅맥");
			assertThat(result.getDescription()).isEqualTo("빅맥입니다.");
			assertThat(result.getPrice()).isEqualTo(6000);
		}

		@Test
		@DisplayName("존재하지 않는 식당을 조회할때 menu 단건 조회 실패")
		public void fail1_findOne() {

			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> menuService.findOne(-1L, 1L))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 메뉴를 조회할때 menu 단건 조회 실패")
		public void fail2_findOne() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(menuRepository.findById(-1L)).thenThrow(NoSuchMenuException.class);

			// then
			assertThatThrownBy(() -> menuService.findOne(1L, -1L))
					.isInstanceOf(NoSuchMenuException.class)
					.hasMessage("존재하지 않는 메뉴입니다.");
		}

		@Test
		@DisplayName("조회한 식당과 메뉴에 등록된 식당이 다를때 menu 단건 조회 실패")
		public void fail3_findOne() {
			// when
			when(restaurantRepository.findById(2L)).thenReturn(Optional.of(restaurant2));
			when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

			// then
			assertThatThrownBy(() -> menuService.findOne(2L, 1L))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}

	@Nested
	@DisplayName("findAll 메서드는")
	class FindAllCase {
		// given
		Pageable pageable = PageRequest.of(0, 2);
		List<Menu> menuList = List.of(menu, menu2);
		PageImpl<Menu> page = new PageImpl<>(menuList, pageable, menuList.size());

		@Test
		@DisplayName("menu 전체 조회 성공")
		public void success_findAll() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(menuRepository.findAllByRestaurant(restaurant, pageable)).thenReturn(page);

			// then
			Page<MenuFindResponse> result = menuService.findAll(1L, pageable);

			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(result.getTotalElements()).isEqualTo(2);
			assertThat(result.getNumber()).isEqualTo(0);
			assertThat(result.getSize()).isEqualTo(2);
			assertThat(result.getContent())
					.extracting(MenuFindResponse::getName)
					.containsExactly("빅맥", "슈슈버거");
		}

		@Test
		@DisplayName("존재하지 않는 식당의 메뉴를 조회할때 menu 전체 조회 실패")
		public void fail1_findAll() {
			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> menuService.findAll(-1L, pageable))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}
	}

	@Nested
	@DisplayName("update 메서드는")
	class UpdateCase {
		// given
		MenuUpdateRequest request = new MenuUpdateRequest("치킨버거", "치킨버거입니다.", 5000);

		@Test
		@DisplayName("menu 수정 성공")
		public void success_update() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

			// then
			menuService.update(request, 1L, 1L, member);
			MenuFindResponse result = menuService.findOne(1L, 1L);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getName()).isEqualTo("치킨버거");
			assertThat(result.getDescription()).isEqualTo("치킨버거입니다.");
			assertThat(result.getPrice()).isEqualTo(5000);
		}

		@Test
		@DisplayName("존재하지 않는 식당을 수정할때 menu 수정 실패")
		public void fail1_update() {
			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> menuService.update(request, -1L, 1L, member))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 메뉴를 수정할때 menu 수정 실패")
		public void fail2_update() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(menuRepository.findById(-1L)).thenThrow(NoSuchMenuException.class);

			// then
			assertThatThrownBy(() -> menuService.update(request, 1L, -1L, member))
					.isInstanceOf(NoSuchMenuException.class)
					.hasMessage("존재하지 않는 메뉴입니다.");
		}

		@Test
		@DisplayName("조회한 식당과 메뉴에 등록된 식당이 다를때 menu 수정 실패")
		public void fail3_update() {
			// when
			when(restaurantRepository.findById(2L)).thenReturn(Optional.of(restaurant2));
			when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

			// then
			assertThatThrownBy(() -> menuService.update(request, 2L, 1L, member))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 식당을 만든 유저가 다를때 menu 수정 실패")
		public void fail4_update() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

			// then
			assertThatThrownBy(() -> menuService.update(request, 1L, 1L, member2))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}

	@Nested
	@DisplayName("delete 메서드는")
	class DeleteCase {

		@Test
		@DisplayName("menu 삭제 성공")
		public void success_delete() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

			// then
			menuService.delete(1L, 1L, member);

			verify(menuRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("존재하지 않는 식당을 삭제할때 menu 삭제 실패")
		public void fail1_delete() {
			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> menuService.delete(-1L, 1L, member))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 메뉴를 삭제할때 menu 삭제 실패")
		public void fail2_delete() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(menuRepository.findById(-1L)).thenThrow(NoSuchMenuException.class);

			// then
			assertThatThrownBy(() -> menuService.delete(1L, -1L, member))
					.isInstanceOf(NoSuchMenuException.class)
					.hasMessage("존재하지 않는 메뉴입니다.");
		}

		@Test
		@DisplayName("조회한 식당과 메뉴에 등록된 식당이 다를때 menu 삭제 실패")
		public void fail3_delete() {
			// when
			when(restaurantRepository.findById(2L)).thenReturn(Optional.of(restaurant2));
			when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

			// then
			assertThatThrownBy(() -> menuService.delete(2L, 1L, member))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 식당을 만든 유저가 다를때 menu 삭제 실패")
		public void fail4_delete() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

			// then
			assertThatThrownBy(() -> menuService.delete(1L, 1L, member2))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}
}
package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.domain.RestaurantCategory;
import com.home.whattoeat.dto.restuarant.RstCategoryCondition;
import com.home.whattoeat.dto.restuarant.RstFindResponse;
import com.home.whattoeat.dto.restuarant.RstSaveRequest;
import com.home.whattoeat.dto.restuarant.RstSaveResponse;
import com.home.whattoeat.dto.restuarant.RstSearchCondition;
import com.home.whattoeat.dto.restuarant.RstSearchKeyword;
import com.home.whattoeat.dto.restuarant.RstUpdateRequest;
import com.home.whattoeat.exception.category.NoSuchCategoryException;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.reataurant.DuplicateRestaurantException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class RestaurantServiceTest extends ServiceTest {

	@InjectMocks
	private RestaurantService restaurantService;

	@Nested
	@DisplayName("save 메서드는")
	class SaveCase {

		// given
		RstSaveRequest request = new RstSaveRequest(
				"맥도날드", "010-1234-1234", "맥도날드 입니다.",
				"안양시", "삼덕로", "123-123",
				1000, 10000, List.of("햄버거", "패스트푸드", "식사용"));

		@Test
		@DisplayName("restaurant 저장 성공")
		public void success_save() {

			// when
			when(restaurantRepository.existsByName(anyString())).thenReturn(false);
			when(categoryRepository.findByName(category.getName())).thenReturn(
					Optional.of(category));
			when(categoryRepository.findByName(category2.getName())).thenReturn(
					Optional.of(category2));
			when(categoryRepository.findByName(category3.getName())).thenReturn(
					Optional.of(category3));
			when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

			// then
			RstSaveResponse result = restaurantService.save(request, member);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getName()).isEqualTo("맥도날드");
			assertThat(result.getPhoneNumber()).isEqualTo("010-1234-1234");
			assertThat(result.getDescription()).isEqualTo("맥도날드 입니다.");
			assertThat(result.getAddress().getCity()).isEqualTo("안양시");
			assertThat(result.getAddress().getStreet()).isEqualTo("삼덕로");
			assertThat(result.getAddress().getZipcode()).isEqualTo("123-123");
			assertThat(result.getStarRating()).isEqualTo(0.0);
			assertThat(result.getNumberOfOrders()).isEqualTo(0L);
			assertThat(result.getMinOrderAmount()).isEqualTo(1000);
			assertThat(result.getMaxOrderAmount()).isEqualTo(10000);
			assertThat(result.getReviewCount()).isEqualTo(0);

			for (RestaurantCategory restaurantCategory : restaurant.getRestaurantCategoryList()) {
				System.out.println("restaurantCategory = " + " "+restaurantCategory.getCategory().getName());
			}
		}

		@Test
		@DisplayName("중복된 식당이름을 등록할때 restaurant 저장 실패")
		public void fail1_save() {

			// when
			when(restaurantRepository.existsByName(request.getName())).thenThrow(
					DuplicateRestaurantException.class);

			// then
			assertThatThrownBy(() -> restaurantService.save(request, member))
					.isInstanceOf(DuplicateRestaurantException.class)
					.hasMessage("이미 존재하는 식당이름입니다.");
		}

		@Test
		@DisplayName("등록되지 않은 카테고리를 입력했을때 restaurant 저장 실패")
		public void fail2_save() {

			// when
			when(restaurantRepository.existsByName(anyString())).thenReturn(false);
			when(categoryRepository.findByName(category.getName())).thenThrow(
					NoSuchCategoryException.class);

			// then
			assertThatThrownBy(() -> restaurantService.save(request, member))
					.isInstanceOf(NoSuchCategoryException.class)
					.hasMessage("존재하지 않는 카테고리입니다.");
		}
	}

	@Nested
	@DisplayName("findOne 메서드는")
	class FindOneCase {

		@Test
		@DisplayName("restaurant 단건 조회 성공")
		public void success_findOne() {

			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

			// then
			RstFindResponse result = restaurantService.findOne(1L);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getName()).isEqualTo("맥도날드");
			assertThat(result.getPhoneNumber()).isEqualTo("010-1234-1234");
			assertThat(result.getDescription()).isEqualTo("맥도날드 입니다.");
			assertThat(result.getAddress().getCity()).isEqualTo("안양시");
			assertThat(result.getAddress().getStreet()).isEqualTo("삼덕로");
			assertThat(result.getAddress().getZipcode()).isEqualTo("123-123");
			assertThat(result.getStarRating()).isEqualTo(0.0);
			assertThat(result.getNumberOfOrders()).isEqualTo(0L);
			assertThat(result.getMinOrderAmount()).isEqualTo(1000);
			assertThat(result.getMaxOrderAmount()).isEqualTo(10000);
			assertThat(result.getReviewCount()).isEqualTo(0);
		}

		@Test
		@DisplayName("존재하지 않는 식당을 조회할때 restaurant 단건 조회 실패")
		public void fail1_findOne() {

			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> restaurantService.findOne(-1L))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}
	}

	@Nested
	@DisplayName("findAllMy 메서드는")
	class FindAllMyCase {

		// given
		PageRequest pageRequest = PageRequest.of(0, 2);
		List<Restaurant> restaurantList = List.of(restaurant, restaurant2);
		PageImpl<Restaurant> page = new PageImpl<>(restaurantList, pageRequest,
				restaurantList.size());

		@Test
		@DisplayName("내가 작성한 restaurant 조회 성공")
		public void success_findAllMy() {

			// when
			when(restaurantRepository.findAllByMember(member, pageRequest)).thenReturn(page);

			// then
			Page<RstFindResponse> result = restaurantService.findAllMy(pageRequest, member);

			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(result.getTotalElements()).isEqualTo(2);
			assertThat(result.getNumber()).isEqualTo(0);
			assertThat(result.getSize()).isEqualTo(2);
			assertThat(result.getContent())
					.extracting(RstFindResponse::getName)
					.containsExactly("맥도날드", "롯데리아");
		}
	}

	@Nested
	@DisplayName("findAllByCategory 메서드는")
	class FindAllByCategoryCase {

		// given
		PageRequest pageRequest = PageRequest.of(0, 2);
		List<Restaurant> restaurantList = List.of(restaurant, restaurant2);
		PageImpl<Restaurant> page = new PageImpl<>(restaurantList, pageRequest,
				restaurantList.size());

		@Test
		@DisplayName("카테고리로 검색한 restaurant 조회 성공")
		public void success_findAllByCategory() {
			RstCategoryCondition condition = new RstCategoryCondition("햄버거");

			// when
			when(restaurantRepository.searchRstByCategory(condition, pageRequest)).thenReturn(page);

			// then
			Page<RstFindResponse> result = restaurantService.findAllByCategory(pageRequest,
					condition);

			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(result.getTotalElements()).isEqualTo(2);
			assertThat(result.getNumber()).isEqualTo(0);
			assertThat(result.getSize()).isEqualTo(2);
			assertThat(result.getContent())
					.extracting(RstFindResponse::getName)
					.containsExactly("맥도날드", "롯데리아");
		}
	}

	@Nested
	@DisplayName("findAllByCondition 메서드는")
	class FindAllByConditionCase {

		// given
		PageRequest pageRequest = PageRequest.of(0, 2);
		List<Restaurant> restaurantList = List.of(restaurant, restaurant2);
		PageImpl<Restaurant> page = new PageImpl<>(restaurantList, pageRequest,
				restaurantList.size());

		@Test
		@DisplayName("조건 검색한 restaurant 조회 성공")
		public void success_findAllByCondition() {
			RstSearchCondition condition = new RstSearchCondition(
					"햄버거", "starRating", 0.0, 900, 9000);

			// when
			when(restaurantRepository.searchRstByCondition(condition, pageRequest)).thenReturn(
					page);

			// then
			Page<RstFindResponse> result = restaurantService.findAllByCondition(pageRequest,
					condition);

			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(result.getTotalElements()).isEqualTo(2);
			assertThat(result.getNumber()).isEqualTo(0);
			assertThat(result.getSize()).isEqualTo(2);
			assertThat(result.getContent())
					.extracting(RstFindResponse::getName)
					.containsExactly("맥도날드", "롯데리아");
		}
	}

	@Nested
	@DisplayName("findAllByKeyword 메서드는")
	class FindAllByKeywordCase {

		// given
		PageRequest pageRequest = PageRequest.of(0, 2);
		List<Restaurant> restaurantList = List.of(restaurant, restaurant2);
		PageImpl<Restaurant> page = new PageImpl<>(restaurantList, pageRequest,
				restaurantList.size());

		@Test
		@DisplayName("키워드 검색한 restaurant 조회 성공")
		public void success_findAllByKeyword() {
			RstSearchKeyword condition = new RstSearchKeyword("패스트푸드");

			// when
			when(restaurantRepository.searchRstByKeyword(condition, pageRequest)).thenReturn(page);

			// then
			Page<RstFindResponse> result = restaurantService.findAllByKeyword(pageRequest,
					condition);

			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(result.getTotalElements()).isEqualTo(2);
			assertThat(result.getNumber()).isEqualTo(0);
			assertThat(result.getSize()).isEqualTo(2);
			assertThat(result.getContent())
					.extracting(RstFindResponse::getName)
					.containsExactly("맥도날드", "롯데리아");
		}
	}

	@Nested
	@DisplayName("update 메서드는")
	class UpdateCase {

		// given
		List<String> cs = new ArrayList<>() {{
			add("햄버거");
			add("패스트푸드");
			add("식사용");
		}};
		RstUpdateRequest request = new RstUpdateRequest(
				"맘스터치", "010-9999-1234", "맘스터치 입니다.",
				"안양시", "삼덕로", "123-123",
				2000, 20000, cs);

		@Test
		@DisplayName("restaurant 수정 성공")
		public void success_update() {

			// UnsupportedOperationException
			// when
//			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
//			when(categoryRepository.findByName(request.getCategoryNames().get(0))).thenReturn(Optional.of(category));
//			when(rcRepository.existsByRestaurant(any(Restaurant.class))).thenReturn(true);
//			when(categoryRepository.findByName(request.getCategoryNames().get(1))).thenReturn(Optional.of(category2));
//			when(categoryRepository.findByName(request.getCategoryNames().get(2))).thenReturn(Optional.of(category3));
//
//			// then
//			restaurantService.update(1L, request, member.getUsername());
////			RstFindResponse result = restaurantService.findOne(1L);
//
//			verify(rcRepository, times(1)).deleteAllByRestaurant(restaurant);
//			assertThat(result.getId()).isEqualTo(1L);
//			assertThat(result.getName()).isEqualTo("맘스터치");
//			assertThat(result.getPhoneNumber()).isEqualTo("010-9999-1234");
//			assertThat(result.getDescription()).isEqualTo("맘스터치 입니다.");
//			assertThat(result.getAddress().getCity()).isEqualTo("안양시");
//			assertThat(result.getAddress().getStreet()).isEqualTo("삼덕로");
//			assertThat(result.getAddress().getZipcode()).isEqualTo("123-123");
//			assertThat(result.getStarRating()).isEqualTo(0.0);
//			assertThat(result.getNumberOfOrders()).isEqualTo(0L);
//			assertThat(result.getMinOrderAmount()).isEqualTo(2000);
//			assertThat(result.getMaxOrderAmount()).isEqualTo(20000);
//			assertThat(result.getReviewCount()).isEqualTo(0);
		}

		@Test
		@DisplayName("존재하지 않는 식당을 조회할때 restaurant 수정 실패")
		public void fail1_update() {

			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> restaurantService.update(-1L, request, member.getUsername()))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 카테고리로 수정할때 restaurant 수정 실패")
		public void fail2_update() {

			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(categoryRepository.findByName(anyString())).thenThrow(NoSuchCategoryException.class);

			// then
			assertThatThrownBy(() -> restaurantService.update(1L, request, member.getUsername()))
					.isInstanceOf(NoSuchCategoryException.class)
					.hasMessage("존재하지 않는 카테고리입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 식당주인이 다를때 restaurant 수정 실패")
		public void fail3_update() {

			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

			// then
			assertThatThrownBy(() -> restaurantService.update(1L, request, "유재석"))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}

	@Nested
	@DisplayName("delete 메서드는")
	class DeleteCase {

		@Test
		@DisplayName("restaurant 삭제 성공")
		public void success_delete() {

			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

			// then
			restaurantService.delete(1L, member.getUsername());

			verify(menuRepository, times(1)).findAllByRestaurant(restaurant);
			verify(orderRepository, times(1)).findAllByRestaurant(restaurant);
			verify(restaurantRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("존재하지 않는 식당을 삭제할때 restaurant 삭제 실패")
		public void fail1_delete() {

			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> restaurantService.delete(-1L, member.getUsername()))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 식당주인이 다를때 restaurant 삭제 실패")
		public void fail2_delete() {

			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

			// then
			assertThatThrownBy(() -> restaurantService.delete(1L, "유재석"))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}
}
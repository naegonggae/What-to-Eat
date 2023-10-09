package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.home.whattoeat.domain.Review;
import com.home.whattoeat.dto.review.ReviewFindResponse;
import com.home.whattoeat.dto.review.ReviewSaveRequest;
import com.home.whattoeat.dto.review.ReviewSaveResponse;
import com.home.whattoeat.dto.review.ReviewUpdateRequest;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import com.home.whattoeat.exception.review.NoSuchReviewException;
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

class ReviewServiceTest extends ServiceTest {

	@InjectMocks
	private ReviewService reviewService;

	@Nested
	@DisplayName("save 메서드는")
	class SaveCase {
		// given
		ReviewSaveRequest request = new ReviewSaveRequest("정말 맛있어요.", 4.5);

		@Test
		@DisplayName("review 생성 성공")
		public void success_save() {

			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(reviewRepository.save(any(Review.class))).thenReturn(review);

			// then
			ReviewSaveResponse result = reviewService.save(request, member, 1L);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getContent()).isEqualTo("정말 맛있어요.");
			assertThat(result.getStarRating()).isEqualTo(4.5);
		}

		@Test
		@DisplayName("존재하지 않는 식당에 리뷰등록할때 review 생성 실패")
		public void fail1_save() {

			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> reviewService.save(request, member, -1L))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}

	}

	@Nested
	@DisplayName("findAll 메서드는")
	class FindAllCase {
		// given
		Pageable pageable = PageRequest.of(0, 2);
		List<Review> reviewList = List.of(review, review2);
		PageImpl<Review> page = new PageImpl<>(reviewList, pageable, reviewList.size());

		@Test
		@DisplayName("식당의 review 전체 조회 성공")
		public void success_findAll() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(reviewRepository.findAllByRestaurant(restaurant, pageable)).thenReturn(page);

			// then
			Page<ReviewFindResponse> result = reviewService.findAll(1L, pageable);

			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(result.getTotalElements()).isEqualTo(2);
			assertThat(result.getNumber()).isEqualTo(0);
			assertThat(result.getSize()).isEqualTo(2);
			assertThat(result.getContent())
					.extracting(ReviewFindResponse::getStarRating)
					.containsExactly(4.5, 1.0);
		}

		@Test
		@DisplayName("존재하지 않는 식당의 리뷰를 조회할때 review 전체 조회 실패")
		public void fail1_findAll() {
			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> reviewService.findAll(-1L, pageable))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}
	}

	@Nested
	@DisplayName("update 메서드는")
	class UpdateCase {
		// given
		ReviewUpdateRequest request = new ReviewUpdateRequest("정말 맛없어요.", 1.0);

		@Test
		@DisplayName("review 수정 성공")
		public void success_update() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

			// then
			reviewService.update(request, 1L, 1L, member);

			verify(restaurantRepository, times(1)).findById(1L);
			verify(reviewRepository, times(1)).findById(1L);
		}

		@Test
		@DisplayName("존재하지 않는 식당을 수정할때 review 수정 실패")
		public void fail1_update() {
			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> reviewService.update(request, -1L, 1L, member))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 리뷰를 수정할때 review 수정 실패")
		public void fail2_update() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(reviewRepository.findById(-1L)).thenThrow(NoSuchReviewException.class);

			// then
			assertThatThrownBy(() -> reviewService.update(request, 1L, -1L, member))
					.isInstanceOf(NoSuchReviewException.class)
					.hasMessage("존재하지 않는 리뷰입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 식당을 만든 유저가 다를때 review 수정 실패")
		public void fail3_update() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

			// then
			assertThatThrownBy(() -> reviewService.update(request, 1L, 1L, member2))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}

	@Nested
	@DisplayName("delete 메서드는")
	class DeleteCase {

		@Test
		@DisplayName("review 삭제 성공")
		public void success_delete() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

			// then
			reviewService.delete(1L, 1L, member);

			verify(reviewRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("존재하지 않는 식당을 삭제할때 review 삭제 실패")
		public void fail1_delete() {
			// when
			when(restaurantRepository.findById(-1L)).thenThrow(NoSuchRestaurantException.class);

			// then
			assertThatThrownBy(() -> reviewService.delete(-1L, 1L, member))
					.isInstanceOf(NoSuchRestaurantException.class)
					.hasMessage("존재하지 않는 음식점입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 메뉴를 삭제할때 review 삭제 실패")
		public void fail2_delete() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(reviewRepository.findById(-1L)).thenThrow(NoSuchReviewException.class);

			// then
			assertThatThrownBy(() -> reviewService.delete(1L, -1L, member))
					.isInstanceOf(NoSuchReviewException.class)
					.hasMessage("존재하지 않는 리뷰입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 식당을 만든 유저가 다를때 review 삭제 실패")
		public void fail3_delete() {
			// when
			when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

			// then
			assertThatThrownBy(() -> reviewService.delete(1L, 1L, member2))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}

}
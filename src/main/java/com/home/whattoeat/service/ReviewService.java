package com.home.whattoeat.service;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.domain.Review;
import com.home.whattoeat.dto.restuarant.RstFindAllResponse;
import com.home.whattoeat.dto.review.ReviewFindResponse;
import com.home.whattoeat.dto.review.ReviewSaveRequest;
import com.home.whattoeat.dto.review.ReviewSaveResponse;
import com.home.whattoeat.dto.review.ReviewUpdateRequest;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import com.home.whattoeat.exception.review.NoSuchReviewException;
import com.home.whattoeat.repository.ReviewRepository;
import com.home.whattoeat.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final RestaurantRepository restaurantRepository;

	// 생성
	@Transactional
	public ReviewSaveResponse save(ReviewSaveRequest request, Member member, Long rstId) {
		Restaurant findRestaurant = restaurantRepository.findById(rstId)
				.orElseThrow(NoSuchRestaurantException::new);

		Review review = Review.createReview(request, findRestaurant, member);

		findRestaurant.updateInfo(request);

		Review savedReview = reviewRepository.save(review);
		return ReviewSaveResponse.from(savedReview.getId());
	}

	// 전체 조회
	public Page<ReviewFindResponse> findAll(Long rstId, Pageable pageable) {

		Restaurant findRestaurant = restaurantRepository.findById(rstId)
				.orElseThrow(NoSuchRestaurantException::new);
		return reviewRepository.findAllByRestaurant(findRestaurant, pageable).map(ReviewFindResponse::from);
	}

	// 수정
	@Transactional
	public void update(ReviewUpdateRequest request, Long rstId, Long reviewId, Member member) {
		Restaurant findRestaurant = restaurantRepository.findById(rstId)
				.orElseThrow(NoSuchRestaurantException::new);

		Review findReview = reviewRepository.findById(reviewId)
				.orElseThrow(NoSuchReviewException::new);

		Double oldStarRating = findReview.getStarRating();

		if (!findReview.getMember().getUsername().equals(member.getUsername())) {
			throw new AccessDeniedException();
		}

		findReview.update(request);

		findRestaurant.updateNewRatingInfo(request, oldStarRating);
	}

	// 삭제
	@Transactional
	public void delete(Long rstId, Long reviewId, Member member) {
		Restaurant findRestaurant = restaurantRepository.findById(rstId)
				.orElseThrow(NoSuchRestaurantException::new);

		Review findReview = reviewRepository.findById(reviewId)
				.orElseThrow(NoSuchReviewException::new);


		if (!findReview.getMember().getUsername().equals(member.getUsername())) {
			throw new AccessDeniedException();
		}


		findReview.softDelete();

		findRestaurant.excludeRatings(findReview.getStarRating());
	}
}

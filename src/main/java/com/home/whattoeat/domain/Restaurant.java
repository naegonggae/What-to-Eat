package com.home.whattoeat.domain;

import com.home.whattoeat.dto.restuarant.RstSaveRequest;
import com.home.whattoeat.dto.restuarant.RstUpdateRequest;
import com.home.whattoeat.dto.review.ReviewSaveRequest;
import com.home.whattoeat.dto.review.ReviewUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "restaurant_id")
	private Long id;
	private String name;
	private String phoneNumber;
	private String cuisineType; // enum

	private Address address;
	private double starRating; // 리뷰할때 구현
	private Long numberOfOrders; // 리뷰할때 구현 지금은 하드 코딩해버리자
	private int minOrderAmount;
	private int maxOrderAmount;
	private int reviewCount;

	// 영업시간
	// 가게 소개
	// 평균가격
	// 사진
	// 예약 가능여부
	// 위도경도
	// 평점
	// 운영자 정보
	// 온라인 예약 링크, 인스타 링크
	// 결제 수단
	// 예약 현황
	// 운영 중단 일정
	// 프로모션 정보

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "restaurant")
	private List<Menu> menuList = new ArrayList<>();

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
	private List<RestaurantCategory> restaurantCategories = new ArrayList<>();

	// 연관관계 메서드 //
	public void addRestaurantCategory(RestaurantCategory restaurantCategory) {
		restaurantCategories.add(restaurantCategory);
		restaurantCategory.addRestaurant(this);
	}

	// 비즈니스 메서드 //
	public void update(RstUpdateRequest request, List<RestaurantCategory> restaurantCategories) {
		this.name = request.getName();
		this.phoneNumber = request.getPhoneNumber();
		this.starRating = request.getStarRating();
		this.numberOfOrders = request.getNumberOfOrders();
		this.minOrderAmount = request.getMinOrderAmount();
		this.maxOrderAmount = request.getMaxOrderAmount();
		this.restaurantCategories = restaurantCategories;
	}

	// 생성 메서드 //
	// 식당 생성
	public static Restaurant createRestaurant(RstSaveRequest request, Member member, List<RestaurantCategory> restaurantCategories){
		return new Restaurant(request, member, restaurantCategories);
	}
	public Restaurant(RstSaveRequest request, Member member, List<RestaurantCategory> restaurantCategories) {
		this.name = request.getName();
		this.phoneNumber = request.getPhoneNumber();
		this.starRating = request.getStarRating();
		this.numberOfOrders = request.getNumberOfOrders();
		this.minOrderAmount = request.getMinOrderAmount();
		this.maxOrderAmount = request.getMaxOrderAmount();
		this.member = member;
		for (RestaurantCategory rc : restaurantCategories) {
			this.addRestaurantCategory(rc);
			// 연관관계 메서드 어떻게 반영하지 그리고 ... 이거 꼭 써야하나 하나씩 저장하고 있는데
			// 이거다 ㅎㅎ 카테고리를 여러개 쓰고 저장을 하면 이렇게 될 수 밖에 없음
			// 나중에 파라미터가 아래 처럼 나올 수 있기 때문에
			// (request, member, restaurantCategory, restaurantCategory, restaurantCategory, restaurantCategory)
			// 그리고 생성하면서 반대편도 반영시켜줌 -> 연관관계 메서드
		}
	}


	public void updateInfo(ReviewSaveRequest request) {
		this.starRating = getAvgRating(request.getStarRating());
		this.reviewCount++;
		this.numberOfOrders++;
	}

	public double getAvgRating(Double newRating) {
		System.out.println("getAvgRating===========");
		System.out.println("starRating = " + starRating);
		System.out.println("reviewCount = " + reviewCount);
		System.out.println("newRating = " + newRating);
		if (reviewCount == 0) {
			return newRating;
		} else {
			double ratingSum = starRating * reviewCount;
			System.out.println("ratingSum = " + ratingSum);
			ratingSum += newRating;
			System.out.println("ratingSum = " + ratingSum);
			double newAvgRating = ratingSum / (reviewCount + 1);
			System.out.println("newAvgRating = " + newAvgRating);
			double roundedRating = Math.round(newAvgRating * 10.0) / 10.0;

			return roundedRating;
		}
	}

	public void updateNewRatingInfo(ReviewUpdateRequest request, Double oldRating) {
		this.starRating = getUpdateAvgRating(request.getStarRating(), oldRating);
	}
	public double getUpdateAvgRating(Double newRating, Double oldRating) {
		System.out.println("getUpdateAvgRating===========");
		System.out.println("starRating = " + starRating);
		System.out.println("reviewCount = " + reviewCount);
		System.out.println("newRating = " + newRating);
		if (reviewCount - 1 == 0) {
			return newRating;
		} else {
			double ratingSum = starRating * reviewCount;
			System.out.println("ratingSum = " + ratingSum);

			ratingSum = ratingSum + newRating - oldRating;
			System.out.println("ratingSum = " + ratingSum);

			double newAvgRating = ratingSum / (reviewCount);
			System.out.println("newAvgRating = " + newAvgRating);
			double roundedRating = Math.round(newAvgRating * 10.0) / 10.0;

			return roundedRating;
		}
	}

	public void excludeRatings(Double rating) {
		this.starRating = deleteRating(rating);
		this.reviewCount--;
	}
	public Double deleteRating(Double rating) {
		System.out.println("deleteRating===========");
		System.out.println("starRating = " + starRating);
		System.out.println("reviewCount = " + reviewCount);
		System.out.println("rating = " + rating);
		if (reviewCount - 1 == 0) {
			return 0.0;
		} else {
			double ratingSum = starRating * reviewCount;
			System.out.println("ratingSum = " + ratingSum);

			ratingSum -= rating;
			System.out.println("ratingSum = " + ratingSum);

			double newAvgRating = ratingSum / (reviewCount - 1);
			System.out.println("newAvgRating = " + newAvgRating);
			double roundedRating = Math.round(newAvgRating * 10.0) / 10.0;

			return roundedRating;
		}
	}
}

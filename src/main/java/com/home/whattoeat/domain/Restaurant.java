package com.home.whattoeat.domain;

import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.home.whattoeat.dto.restuarant.RstSaveRequest;
import com.home.whattoeat.dto.restuarant.RstUpdateRequest;
import com.home.whattoeat.dto.review.ReviewSaveRequest;
import com.home.whattoeat.dto.review.ReviewUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class Restaurant extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "restaurant_id")
	private Long id;
	private String name;
	private String phoneNumber;
	private String description;
	@Embedded
	private Address address;
	private Double starRating;
	private Long numberOfOrders;
	private Integer minOrderAmount;
	private Integer maxOrderAmount;
	private Integer reviewCount;
	@Enumerated(EnumType.STRING)
	private RestaurantStatus status; // OPEN, CLOSED, VACATION; // 사장님 전용어플 만들 때까지 항상 OPEN 으로 유지
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE) // 식당을 삭제하면 메뉴도 삭제
	private List<Menu> menuList = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE) // 식당을 삭제하면 식당리뷰도 삭제
	private List<Review> reviewList = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
	private List<RestaurantCategory> restaurantCategoryList = new ArrayList<>();

	// 연관관계 메서드 //
	public void addRestaurantCategory(RestaurantCategory restaurantCategory) {
		restaurantCategoryList.add(restaurantCategory);
		restaurantCategory.addRestaurant(this);
	}

	// 생성 메서드 //
	public static Restaurant createRestaurant(RstSaveRequest request, Member member, List<RestaurantCategory> restaurantCategoryList){
		return new Restaurant(request, member, restaurantCategoryList);
	}
	public Restaurant(RstSaveRequest request, Member member, List<RestaurantCategory> restaurantCategoryList) {
		this.name = request.getName();
		this.phoneNumber = request.getPhoneNumber();
		this.description = request.getDescription();
		this.address = Address.createAddress(request.getCity(), request.getStreet(), request.getZipcode());
		this.starRating = 0.0;
		this.numberOfOrders = 0L;
		this.minOrderAmount = request.getMinOrderAmount();
		this.maxOrderAmount = request.getMaxOrderAmount();
		for (RestaurantCategory rc : restaurantCategoryList) {
			this.addRestaurantCategory(rc);
		}
		this.reviewCount = 0;
		this.status = RestaurantStatus.OPEN;
		this.member = member;
	}

	// 비즈니스 메서드 //
	public void update(RstUpdateRequest request, List<RestaurantCategory> restaurantCategoryList) {
		this.name = request.getName();
		this.phoneNumber = request.getPhoneNumber();
		this.description = request.getDescription();
		this.address = Address.createAddress(request.getCity(), request.getStreet(),
				request.getZipcode());
		this.minOrderAmount = request.getMinOrderAmount();
		this.maxOrderAmount = request.getMaxOrderAmount();
		for (RestaurantCategory rc : restaurantCategoryList) {
			this.addRestaurantCategory(rc);
		}
	}

	// 리뷰를 작성하면 오를게 아니라 주문을 할때 올라야할듯 주문수만
	// 회원이 리뷰를 삭제했을때도 별점 관리 해줘 리뷰수랑
	public void updateInfo(ReviewSaveRequest request) {
		this.starRating = getAvgRating(request.getStarRating());
		this.reviewCount++;
		this.numberOfOrders++;
	}

	// 별점은 더블로 소수점단위로 보여주다가 화면에 보여줄때만 소수점 첫째까지만 보여줘
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

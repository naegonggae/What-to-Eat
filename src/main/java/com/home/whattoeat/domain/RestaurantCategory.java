package com.home.whattoeat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
//@Builder
//@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
//@AllArgsConstructor
public class RestaurantCategory {

	@Id @GeneratedValue
	@Column(name = "restaurant_category_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY) // 뭔가 지워도 될 느낌
	private Restaurant restaurant;

	// 연관관계 메서드 //
	public void addRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	// 생성 메서드 //
	public static RestaurantCategory createCategory(Category category) {
		return new RestaurantCategory(category);
	}
	public RestaurantCategory(Category category) {
		this.category = category;
	}


//	public void addRestaurantCategory(RestaurantCategory rc) {
//	}
}

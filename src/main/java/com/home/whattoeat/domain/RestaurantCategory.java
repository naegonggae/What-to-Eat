package com.home.whattoeat.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class RestaurantCategory {

	@Id @GeneratedValue
	@Column(name = "restaurant_category_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	private Restaurant restaurant;

	// 연관관계 메서드 // 없애면 절대 안됨 없애면 식당을 등록했을때 레스토랑 카테고리에 레스토랑 아이디맵핑이 안됨
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

}

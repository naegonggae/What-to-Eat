package com.home.whattoeat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class RestaurantCategory {

	@Id @GeneratedValue
	@Column(name = "restaurant_category_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	private Restaurant restaurant;


}

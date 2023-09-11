package com.home.whattoeat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Restaurant {

	@Id @GeneratedValue
	@Column(name = "restaurant_id")
	private Long id;
	private String name;
	private String phoneNumber;
	private String cuisineType; // enum

	@OneToMany(mappedBy = "menu")
	private List<Menu> menuList = new ArrayList<>();

}

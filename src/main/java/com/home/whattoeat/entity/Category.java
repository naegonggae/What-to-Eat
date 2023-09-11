package com.home.whattoeat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import lombok.Getter;

@Entity
@Getter
public class Category {

	@Id @GeneratedValue
	@Column(name = "category_id")
	private Long id;
	private String name;
	@OneToMany(mappedBy = "menu")
	private List<Category> categoryList = new ArrayList<>();

}

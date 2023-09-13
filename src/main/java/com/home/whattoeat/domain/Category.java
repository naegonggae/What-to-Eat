package com.home.whattoeat.domain;

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
public class Category {

	@Id @GeneratedValue
	@Column(name = "category_id")
	private Long id;
	private String name;
	@OneToMany(mappedBy = "category")
	private List<Menu> menuList = new ArrayList<>();

}

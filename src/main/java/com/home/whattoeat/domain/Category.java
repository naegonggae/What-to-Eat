package com.home.whattoeat.domain;

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
import lombok.Getter;

@Entity
@Getter
public class Category extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "category_id")
	private Long id;
	private String name;
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<RestaurantCategory> restaurantList = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Category parent; // 셀프 양방향 걸기 / 대분류 소분류

	@OneToMany(mappedBy = "parent")
	private List<Category> child = new ArrayList<>();

	// 연관관계 편의 메서드
//	public void addChildCategory(Category child) {
//		this.child.add(child);
//		child.setParent(this);
//
//	}
//	public void addParent(Category child) {
//		this.parent = this;
//	}
}

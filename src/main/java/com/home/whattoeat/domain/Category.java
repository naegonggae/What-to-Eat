package com.home.whattoeat.domain;

import com.home.whattoeat.dto.category.CategoryUpdateRequest;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "category_id")
	private Long id;
	private String name;
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<RestaurantCategory> restaurantList = new ArrayList<>();

	// 생성 메서드 //
	public static Category createCategory(String categoryName) {
		return new Category(categoryName);
	}
	public Category(String name) {
		this.name = name;
	}

	public void update(CategoryUpdateRequest request) {
		this.name = request.getCategoryName();
	}

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "parent_id")
//	private Category parent; // 셀프 양방향 걸기 / 대분류 소분류
//
//	@OneToMany(mappedBy = "parent")
//	private List<Category> child = new ArrayList<>();

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

package com.home.whattoeat.domain;

import static lombok.AccessLevel.PROTECTED;

import com.home.whattoeat.dto.category.CategoryUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class Category extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "category_id")
	private Long id;
	private String name;

	// 생성 메서드 //
	public static Category createCategory(String categoryName) {
		return new Category(categoryName);
	}
	public Category(String name) {
		this.name = name;
	}

	// 비즈니스 메서드
	public void update(CategoryUpdateRequest request) {
		this.name = request.getCategoryName();
	}

}

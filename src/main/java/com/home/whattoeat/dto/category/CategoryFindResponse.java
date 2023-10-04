package com.home.whattoeat.dto.category;

import com.home.whattoeat.domain.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryFindResponse {

	private Long id;
	private String name;

	public static CategoryFindResponse from(Category category) {
		return new CategoryFindResponse(category.getId(), category.getName());
	}

}

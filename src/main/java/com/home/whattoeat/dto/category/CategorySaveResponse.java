package com.home.whattoeat.dto.category;

import com.home.whattoeat.domain.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CategorySaveResponse {

	private Long id;
	private String name;

	public static CategorySaveResponse from(Category category) {
		return new CategorySaveResponse(category.getId(), category.getName());
	}

}

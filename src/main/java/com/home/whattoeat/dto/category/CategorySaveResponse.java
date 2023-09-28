package com.home.whattoeat.dto.category;

import com.home.whattoeat.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategorySaveResponse {

	private Long id;

	public static CategorySaveResponse from(Category category) {
		return new CategorySaveResponse(category.getId());
	}

}

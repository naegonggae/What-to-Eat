package com.home.whattoeat.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryFindResponse {

	private Long id;

	public static CategoryFindResponse from(Long id) {
		return new CategoryFindResponse(id);
	}

}

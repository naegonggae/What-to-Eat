package com.home.whattoeat.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentSaveResponse {

	private Long id;

	public static CommentSaveResponse from(Long id) {
		return new CommentSaveResponse(id);
	}
}

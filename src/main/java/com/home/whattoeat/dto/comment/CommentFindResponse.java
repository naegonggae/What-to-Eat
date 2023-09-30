package com.home.whattoeat.dto.comment;

import com.home.whattoeat.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentFindResponse {

	private Long id;

	public static CommentFindResponse from(Comment comment) {
		return new CommentFindResponse(comment.getId());
	}

}

package com.home.whattoeat.dto.comment;

import com.home.whattoeat.domain.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentFindResponse {

	private Long id;
	private String content;

	public static CommentFindResponse from(Comment comment) {
		return new CommentFindResponse(comment.getId(), comment.getContent());
	}

}

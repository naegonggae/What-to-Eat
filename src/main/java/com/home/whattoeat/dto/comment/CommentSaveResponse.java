package com.home.whattoeat.dto.comment;

import com.home.whattoeat.domain.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentSaveResponse {

	private Long id;
	private String content;

	public static CommentSaveResponse from(Comment comment) {
		return new CommentSaveResponse(comment.getId(), comment.getContent());
	}
}

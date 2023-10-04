package com.home.whattoeat.dto.reply;

import com.home.whattoeat.domain.Reply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyFindResponse {

	private Long id;
	private String content;

	public static ReplyFindResponse from(Reply reply) {
		return new ReplyFindResponse(reply.getId(), reply.getContent());
	}

}

package com.home.whattoeat.dto.reply;

import com.home.whattoeat.domain.Reply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplySaveResponse {

	private Long id;
	private String content;

	public static ReplySaveResponse from(Reply reply) {
		return new ReplySaveResponse(reply.getId(), reply.getContent());
	}

}

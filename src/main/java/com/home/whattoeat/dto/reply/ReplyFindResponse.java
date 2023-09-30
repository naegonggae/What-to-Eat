package com.home.whattoeat.dto.reply;

import com.home.whattoeat.domain.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyFindResponse {

	private Long id;

	public static ReplyFindResponse from(Reply reply) {
		return new ReplyFindResponse(reply.getId());
	}

}

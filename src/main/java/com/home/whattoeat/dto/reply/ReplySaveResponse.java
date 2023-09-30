package com.home.whattoeat.dto.reply;

import com.home.whattoeat.domain.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReplySaveResponse {

	private Long id;

	public static ReplySaveResponse from(Reply reply) {
		return new ReplySaveResponse(reply.getId());
	}

}

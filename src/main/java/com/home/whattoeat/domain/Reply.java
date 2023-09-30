package com.home.whattoeat.domain;

import com.home.whattoeat.dto.reply.ReplySaveRequest;
import com.home.whattoeat.dto.reply.ReplyUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Reply extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "reply_id")
	private Long id;

	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id")
	private Comment comment;

	public static Reply createReply(ReplySaveRequest request, Comment findComment, Member member) {
		return new Reply(request.getContent(), member, findComment);
	}
	public static Reply createOwnerReply(ReplySaveRequest request, Comment findComment, Member member) {
		return new Reply("가게 사장님이 작성한 댓글입니다.\n"+request.getContent(), member, findComment);
	}
	public Reply(String content, Member member, Comment comment) {
		this.content = content;
		this.member = member;
		this.comment = comment;
	}

	public void update(ReplyUpdateRequest request) {
		this.content = request.getContent();
	}
}

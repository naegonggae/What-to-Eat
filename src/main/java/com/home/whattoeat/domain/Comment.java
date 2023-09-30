package com.home.whattoeat.domain;

import com.home.whattoeat.dto.comment.CommentSaveRequest;
import com.home.whattoeat.dto.comment.CommentUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "comment_id")
	private Long id;

	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private Review review;

	@OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
	private List<Reply> replies = new ArrayList<>();

	public static Comment createComment(CommentSaveRequest request, Review findReview,
			Member member) {
		return new Comment(request.getContent(), member, findReview);
	}
	public static Comment createOwnerComment(CommentSaveRequest request, Review findReview,
			Member member) {
		return new Comment("가게 사장님이 작성한 댓글입니다.\n"+request.getContent(), member, findReview);
	}
	public Comment(String content, Member member, Review review) {
		this.content = content;
		this.member = member;
		this.review = review;
	}

	public void update(CommentUpdateRequest request) {
		this.content = request.getContent();
	}
}

package com.home.whattoeat.domain;

import static lombok.AccessLevel.PROTECTED;

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
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
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

	@OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE) // 댓글이 삭제 될때 대댓글들도 삭제
	private List<Reply> replyList = new ArrayList<>();

	// 연관관계 메서드 //
	private void addReview(Review review) {
		this.review = review;
		review.getComments().add(this);
	}

	// 생성 메서드 //
	public static Comment createComment(CommentSaveRequest request, Review findReview, Member member) {
		return new Comment(request.getContent(), member, findReview);
	}
	public static Comment createOwnerComment(CommentSaveRequest request, Review findReview, Member member) {
		return new Comment("가게 사장님이 작성한 댓글입니다.\n" + request.getContent(), member,
				findReview);
	}
	public Comment(String content, Member member, Review review) {
		this.content = content;
		this.member = member;
		this.addReview(review);
	}

	// 비즈니스 메서드 //
	public void update(CommentUpdateRequest request) {
		this.content = request.getContent();
	}
}

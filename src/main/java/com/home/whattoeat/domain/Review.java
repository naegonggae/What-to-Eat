package com.home.whattoeat.domain;

import com.home.whattoeat.dto.review.ReviewSaveRequest;
import com.home.whattoeat.dto.review.ReviewUpdateRequest;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "review_id")
	private Long id;
	private String content;
	private Double starRating;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	@OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
	private List<Comment> comments = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public static Review createReview(ReviewSaveRequest request, Restaurant restaurant, Member member) {
		return new Review(request.getContent(), request.getStarRating(), restaurant, member);
	}
	public Review(String content, Double starRating, Restaurant restaurant, Member member) {
		this.content = content;
		this.starRating = starRating;
		this.restaurant = restaurant;
		this.member = member;
	}

	public void update(ReviewUpdateRequest request) {
		this.content = request.getContent();
		this.starRating = request.getStarRating();
	}
}

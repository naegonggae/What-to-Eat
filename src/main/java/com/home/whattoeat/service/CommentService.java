package com.home.whattoeat.service;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Comment;
import com.home.whattoeat.domain.Review;
import com.home.whattoeat.dto.comment.CommentSaveResponse;
import com.home.whattoeat.dto.comment.CommentFindResponse;
import com.home.whattoeat.dto.comment.CommentSaveRequest;
import com.home.whattoeat.dto.comment.CommentUpdateRequest;
import com.home.whattoeat.exception.comment.NoSuchCommentException;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.review.NoSuchReviewException;
import com.home.whattoeat.repository.CommentRepository;
import com.home.whattoeat.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final ReviewRepository reviewRepository;

	@Transactional
	public CommentSaveResponse save(CommentSaveRequest request, Member member, Long reviewId) {
		Review findReview = reviewRepository.findById(reviewId)
				.orElseThrow(NoSuchReviewException::new);

		Comment comment = Comment.createComment(request, findReview, member);

		Comment savedComment = commentRepository.save(comment);
		return CommentSaveResponse.from(savedComment.getId());
	}

	// 리뷰내 댓글 전체 조회
	public Page<CommentFindResponse> findAll(Long rstId, Pageable pageable) {

		Review findReview = reviewRepository.findById(rstId)
				.orElseThrow(NoSuchReviewException::new);
		return commentRepository.findAllByReview(findReview, pageable).map(CommentFindResponse::from);
	}

	// 수정
	@Transactional
	public void update(CommentUpdateRequest request, Long reviewId, Long cmtId, Member member) {
		Review findReview = reviewRepository.findById(reviewId)
				.orElseThrow(NoSuchReviewException::new);

		Comment findComment = commentRepository.findById(cmtId)
				.orElseThrow(NoSuchCommentException::new);

		if (!findComment.getMember().getUsername().equals(member.getUsername())) {
			throw new AccessDeniedException();
		}

		findComment.update(request);
	}

	// 삭제
	@Transactional
	public void delete(Long reviewId, Long cmtId, Member member) {
		Review findReview = reviewRepository.findById(reviewId)
				.orElseThrow(NoSuchReviewException::new);

		Comment findComment = commentRepository.findById(cmtId)
				.orElseThrow(NoSuchCommentException::new);

		if (!findComment.getMember().getUsername().equals(member.getUsername())) {
			throw new AccessDeniedException();
		}

		findComment.softDelete();
	}

}

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

		// 일반손님용
		Comment comment = Comment.createComment(request, findReview, member);

		// 댓글 다는 사람이 사장님이면 "가게사장님입니다." 표시
		if (member.getUsername().equals(findReview.getRestaurant().getMember().getUsername())) {
			comment = Comment.createOwnerComment(request, findReview, member);
		}

		Comment savedComment = commentRepository.save(comment);
		return CommentSaveResponse.from(savedComment);
	}

	// 리뷰내 댓글 전체 조회
	public Page<CommentFindResponse> findAll(Long rstId, Pageable pageable) {
		Review findReview = reviewRepository.findById(rstId)
				.orElseThrow(NoSuchReviewException::new);
		return commentRepository.findAllByReview(findReview, pageable).map(CommentFindResponse::from);
	}

	// 댓글 수정
	@Transactional
	public void update(CommentUpdateRequest request, Long reviewId, Long cmtId, Member member) {
		Comment findComment = isExistedReviewAndComment(reviewId, cmtId);

		hasPermission(member, findComment);

		findComment.update(request);
	}


	// 삭제
	@Transactional
	public void delete(Long reviewId, Long cmtId, Member member) {
		Comment findComment = isExistedReviewAndComment(reviewId, cmtId);

		hasPermission(member, findComment);

		commentRepository.deleteById(cmtId);
	}

	private Comment isExistedReviewAndComment(Long reviewId, Long cmtId) {
		Review findReview = reviewRepository.findById(reviewId)
				.orElseThrow(NoSuchReviewException::new);

		Comment findComment = commentRepository.findById(cmtId)
				.orElseThrow(NoSuchCommentException::new);
		return findComment;
	}

	private static void hasPermission(Member member, Comment findComment) {
		if (!findComment.getMember().getUsername().equals(member.getUsername())) {
			throw new AccessDeniedException();
		}
	}

}

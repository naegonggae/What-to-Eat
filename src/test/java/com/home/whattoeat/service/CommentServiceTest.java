package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.home.whattoeat.domain.Comment;
import com.home.whattoeat.dto.comment.CommentFindResponse;
import com.home.whattoeat.dto.comment.CommentSaveRequest;
import com.home.whattoeat.dto.comment.CommentSaveResponse;
import com.home.whattoeat.dto.comment.CommentUpdateRequest;
import com.home.whattoeat.exception.comment.NoSuchCommentException;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.review.NoSuchReviewException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class CommentServiceTest extends ServiceTest {

	@InjectMocks
	private CommentService commentService;

	@Nested
	@DisplayName("save 메서드는")
	class SaveCase {
		// given
		CommentSaveRequest request = new CommentSaveRequest("공감해요");

		@Test
		@DisplayName("comment 생성 성공")
		public void success_save() {

			// when
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
			when(commentRepository.save(any(Comment.class))).thenReturn(comment);

			// then
			CommentSaveResponse result = commentService.save(request, member, 1L);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getContent()).isEqualTo("공감해요");
		}

		@Test
		@DisplayName("존재하지 않는 리뷰에 댓글등록할때 comment 생성 실패")
		public void fail1_save() {

			// when
			when(reviewRepository.findById(-1L)).thenThrow(NoSuchReviewException.class);

			// then
			assertThatThrownBy(() -> commentService.save(request, member, -1L))
					.isInstanceOf(NoSuchReviewException.class)
					.hasMessage("존재하지 않는 리뷰입니다.");
		}

	}

	@Nested
	@DisplayName("findAll 메서드는")
	class FindAllCase {
		// given
		Pageable pageable = PageRequest.of(0, 2);
		List<Comment> commentList = List.of(comment, comment2);
		PageImpl<Comment> page = new PageImpl<>(commentList, pageable, commentList.size());

		@Test
		@DisplayName("리뷰의 comment 전체 조회 성공")
		public void success_findAll() {
			// when
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
			when(commentRepository.findAllByReview(review, pageable)).thenReturn(page);

			// then
			Page<CommentFindResponse> result = commentService.findAll(1L, pageable);

			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(result.getTotalElements()).isEqualTo(2);
			assertThat(result.getNumber()).isEqualTo(0);
			assertThat(result.getSize()).isEqualTo(2);
			assertThat(result.getContent())
					.extracting(CommentFindResponse::getContent)
					.containsExactly("공감해요", "공감안해요");
		}

		@Test
		@DisplayName("존재하지 않는 리뷰의 댓글을 조회할때 comment 전체 조회 실패")
		public void fail1_findAll() {
			// when
			when(reviewRepository.findById(-1L)).thenThrow(NoSuchReviewException.class);

			// then
			assertThatThrownBy(() -> commentService.findAll(-1L, pageable))
					.isInstanceOf(NoSuchReviewException.class)
					.hasMessage("존재하지 않는 리뷰입니다.");
		}
	}

	@Nested
	@DisplayName("update 메서드는")
	class UpdateCase {
		// given
		CommentUpdateRequest request = new CommentUpdateRequest("그냥그래요");

		@Test
		@DisplayName("comment 수정 성공")
		public void success_update() {
			// when
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

			// then
			commentService.update(request, 1L, 1L, member);

			verify(reviewRepository, times(1)).findById(1L);
			verify(commentRepository, times(1)).findById(1L);
		}

		@Test
		@DisplayName("존재하지 않는 리뷰를 수정할때 comment 수정 실패")
		public void fail1_update() {
			// when
			when(reviewRepository.findById(-1L)).thenThrow(NoSuchReviewException.class);

			// then
			assertThatThrownBy(() -> commentService.update(request, -1L, 1L, member))
					.isInstanceOf(NoSuchReviewException.class)
					.hasMessage("존재하지 않는 리뷰입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 댓글을 수정할때 comment 수정 실패")
		public void fail2_update() {
			// when
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
			when(commentRepository.findById(-1L)).thenThrow(NoSuchCommentException.class);

			// then
			assertThatThrownBy(() -> commentService.update(request, 1L, -1L, member))
					.isInstanceOf(NoSuchCommentException.class)
					.hasMessage("존재하지 않는 댓글입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 댓글을 만든 유저가 다를때 comment 수정 실패")
		public void fail3_update() {
			// when
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

			// then
			assertThatThrownBy(() -> commentService.update(request, 1L, 1L, member2))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}

	@Nested
	@DisplayName("delete 메서드는")
	class DeleteCase {

		@Test
		@DisplayName("comment 삭제 성공")
		public void success_delete() {
			// when
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

			// then
			commentService.delete(1L, 1L, member);

			verify(commentRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("존재하지 않는 리뷰를 삭제할때 comment 삭제 실패")
		public void fail1_delete() {
			// when
			when(reviewRepository.findById(-1L)).thenThrow(NoSuchReviewException.class);

			// then
			assertThatThrownBy(() -> commentService.delete(-1L, 1L, member))
					.isInstanceOf(NoSuchReviewException.class)
					.hasMessage("존재하지 않는 리뷰입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 댓글를 삭제할때 comment 삭제 실패")
		public void fail2_delete() {
			// when
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
			when(commentRepository.findById(-1L)).thenThrow(NoSuchCommentException.class);

			// then
			assertThatThrownBy(() -> commentService.delete(1L, -1L, member))
					.isInstanceOf(NoSuchCommentException.class)
					.hasMessage("존재하지 않는 댓글입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 댓글을 만든 유저가 다를때 comment 삭제 실패")
		public void fail3_delete() {
			// when
			when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

			// then
			assertThatThrownBy(() -> commentService.delete(1L, 1L, member2))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}

}
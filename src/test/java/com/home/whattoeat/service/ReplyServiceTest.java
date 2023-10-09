package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.home.whattoeat.domain.Reply;
import com.home.whattoeat.dto.reply.ReplyFindResponse;
import com.home.whattoeat.dto.reply.ReplySaveRequest;
import com.home.whattoeat.dto.reply.ReplySaveResponse;
import com.home.whattoeat.dto.reply.ReplyUpdateRequest;
import com.home.whattoeat.exception.comment.NoSuchCommentException;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.reply.NoSuchReplyException;
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

class ReplyServiceTest extends ServiceTest {

	@InjectMocks
	private ReplyService replyService;

	@Nested
	@DisplayName("save 메서드는")
	class SaveCase {
		// given
		ReplySaveRequest request = new ReplySaveRequest("저도요");

		@Test
		@DisplayName("reply 생성 성공")
		public void success_save() {

			// when
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
			when(replyRepository.save(any(Reply.class))).thenReturn(reply);

			// then
			ReplySaveResponse result = replyService.save(request, member, 1L);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getContent()).isEqualTo("저도요");
		}

		@Test
		@DisplayName("존재하지 않는 댓글에 대댓글 등록할때 reply 생성 실패")
		public void fail1_save() {

			// when
			when(commentRepository.findById(-1L)).thenThrow(NoSuchCommentException.class);

			// then
			assertThatThrownBy(() -> replyService.save(request, member, -1L))
					.isInstanceOf(NoSuchCommentException.class)
					.hasMessage("존재하지 않는 댓글입니다.");
		}

	}

	@Nested
	@DisplayName("findAll 메서드는")
	class FindAllCase {
		// given
		Pageable pageable = PageRequest.of(0, 2);
		List<Reply> replyList = List.of(reply, reply2);
		PageImpl<Reply> page = new PageImpl<>(replyList, pageable, replyList.size());

		@Test
		@DisplayName("댓글의 reply 전체 조회 성공")
		public void success_findAll() {
			// when
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
			when(replyRepository.findAllByComment(comment, pageable)).thenReturn(page);

			// then
			Page<ReplyFindResponse> result = replyService.findAll(1L, pageable);

			assertThat(result.getTotalPages()).isEqualTo(1);
			assertThat(result.getTotalElements()).isEqualTo(2);
			assertThat(result.getNumber()).isEqualTo(0);
			assertThat(result.getSize()).isEqualTo(2);
			assertThat(result.getContent())
					.extracting(ReplyFindResponse::getContent)
					.containsExactly("저도요", "난아님");
		}

		@Test
		@DisplayName("존재하지 않는 댓글의 대댓글을 조회할때 reply 전체 조회 실패")
		public void fail1_findAll() {
			// when
			when(commentRepository.findById(-1L)).thenThrow(NoSuchCommentException.class);

			// then
			assertThatThrownBy(() -> replyService.findAll(-1L, pageable))
					.isInstanceOf(NoSuchCommentException.class)
					.hasMessage("존재하지 않는 댓글입니다.");
		}
	}

	@Nested
	@DisplayName("update 메서드는")
	class UpdateCase {
		// given
		ReplyUpdateRequest request = new ReplyUpdateRequest("인정");

		@Test
		@DisplayName("reply 수정 성공")
		public void success_update() {
			// when
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
			when(replyRepository.findById(1L)).thenReturn(Optional.of(reply));

			// then
			replyService.update(request, 1L, 1L, member);

			verify(commentRepository, times(1)).findById(1L);
			verify(replyRepository, times(1)).findById(1L);
		}

		@Test
		@DisplayName("존재하지 않는 댓글을 수정할때 reply 수정 실패")
		public void fail1_update() {
			// when
			when(commentRepository.findById(-1L)).thenThrow(NoSuchCommentException.class);

			// then
			assertThatThrownBy(() -> replyService.update(request, -1L, 1L, member))
					.isInstanceOf(NoSuchCommentException.class)
					.hasMessage("존재하지 않는 댓글입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 대댓글을 수정할때 reply 수정 실패")
		public void fail2_update() {
			// when
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
			when(replyRepository.findById(-1L)).thenThrow(NoSuchReplyException.class);

			// then
			assertThatThrownBy(() -> replyService.update(request, 1L, -1L, member))
					.isInstanceOf(NoSuchReplyException.class)
					.hasMessage("존재하지 않는 대댓글입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 대댓글을 만든 유저가 다를때 reply 수정 실패")
		public void fail3_update() {
			// when
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
			when(replyRepository.findById(1L)).thenReturn(Optional.of(reply));

			// then
			assertThatThrownBy(() -> replyService.update(request, 1L, 1L, member2))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}

	@Nested
	@DisplayName("delete 메서드는")
	class DeleteCase {

		@Test
		@DisplayName("reply 삭제 성공")
		public void success_delete() {
			// when
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
			when(replyRepository.findById(1L)).thenReturn(Optional.of(reply));

			// then
			replyService.delete(1L, 1L, member);

			verify(replyRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("존재하지 않는 댓글을 삭제할때 reply 삭제 실패")
		public void fail1_delete() {
			// when
			when(commentRepository.findById(-1L)).thenThrow(NoSuchCommentException.class);

			// then
			assertThatThrownBy(() -> replyService.delete(-1L, 1L, member))
					.isInstanceOf(NoSuchCommentException.class)
					.hasMessage("존재하지 않는 댓글입니다.");
		}

		@Test
		@DisplayName("존재하지 않는 대댓글를 삭제할때 reply 삭제 실패")
		public void fail2_delete() {
			// when
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
			when(replyRepository.findById(-1L)).thenThrow(NoSuchReplyException.class);

			// then
			assertThatThrownBy(() -> replyService.delete(1L, -1L, member))
					.isInstanceOf(NoSuchReplyException.class)
					.hasMessage("존재하지 않는 대댓글입니다.");
		}

		@Test
		@DisplayName("로그인한 유저와 대댓글을 만든 유저가 다를때 reply 삭제 실패")
		public void fail3_delete() {
			// when
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
			when(replyRepository.findById(1L)).thenReturn(Optional.of(reply));

			// then
			assertThatThrownBy(() -> replyService.delete(1L, 1L, member2))
					.isInstanceOf(AccessDeniedException.class)
					.hasMessage("접근 권한이 없는 요청입니다.");
		}
	}

}
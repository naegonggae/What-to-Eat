package com.home.whattoeat.service;

import com.home.whattoeat.domain.Comment;
import com.home.whattoeat.domain.Reply;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.dto.reply.ReplyFindResponse;
import com.home.whattoeat.dto.reply.ReplySaveRequest;
import com.home.whattoeat.dto.reply.ReplySaveResponse;
import com.home.whattoeat.dto.reply.ReplyUpdateRequest;
import com.home.whattoeat.exception.comment.NoSuchCommentException;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.reply.NoSuchReplyException;
import com.home.whattoeat.repository.CommentRepository;
import com.home.whattoeat.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

	private final ReplyRepository replyRepository;
	private final CommentRepository commentRepository;

	@Transactional
	public ReplySaveResponse save(ReplySaveRequest request, Member member, Long cmtId) {
		Comment findComment = commentRepository.findById(cmtId)
				.orElseThrow(NoSuchCommentException::new);

		Reply reply = Reply.createReply(request, findComment, member);

		Reply savedReply = replyRepository.save(reply);
		return ReplySaveResponse.from(savedReply);
	}

	// 리뷰내 댓글 전체 조회
	public Page<ReplyFindResponse> findAll(Long cmtId, Pageable pageable) {

		Comment findComment = commentRepository.findById(cmtId)
				.orElseThrow(NoSuchCommentException::new);
		return replyRepository.findAllByComment(findComment, pageable).map(ReplyFindResponse::from);
	}

	// 수정
	@Transactional
	public void update(ReplyUpdateRequest request, Long cmtId, Long rpId, Member member) {
		Comment findComment = commentRepository.findById(cmtId)
				.orElseThrow(NoSuchCommentException::new);

		Reply findReply = replyRepository.findById(rpId)
				.orElseThrow(NoSuchReplyException::new);

		if (!findReply.getMember().getUsername().equals(member.getUsername())) {
			throw new AccessDeniedException();
		}

		findReply.update(request);
	}

	// 삭제
	@Transactional
	public void delete(Long cmtId, Long rpId, Member member) {
		Comment findComment = commentRepository.findById(cmtId)
				.orElseThrow(NoSuchCommentException::new);

		Reply findReply = replyRepository.findById(rpId)
				.orElseThrow(NoSuchReplyException::new);

		if (!findReply.getMember().getUsername().equals(member.getUsername())) {
			throw new AccessDeniedException();
		}

		findReply.softDelete();
	}

}

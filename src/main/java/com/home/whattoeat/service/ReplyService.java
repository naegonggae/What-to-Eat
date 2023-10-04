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

	// 대댓글 작성
	@Transactional
	public ReplySaveResponse save(ReplySaveRequest request, Member member, Long cmtId) {
		Comment findComment = commentRepository.findById(cmtId)
				.orElseThrow(NoSuchCommentException::new);

		// 일반 회원용
		Reply reply = Reply.createReply(request, findComment, member);

		// 댓글 다는 사람이 사장님이면 "가게 사장님입니다." 표시
		if (member.getUsername().equals(findComment.getReview().getRestaurant().getMember().getUsername())) {
			reply = Reply.createOwnerReply(request, findComment, member);
		}

		Reply savedReply = replyRepository.save(reply);
		return ReplySaveResponse.from(savedReply);
	}

	// 댓글 내 대댓글들 전체 조회
	public Page<ReplyFindResponse> findAll(Long cmtId, Pageable pageable) {
		Comment findComment = commentRepository.findById(cmtId)
				.orElseThrow(NoSuchCommentException::new);
		return replyRepository.findAllByComment(findComment, pageable).map(ReplyFindResponse::from);
	}

	// 대댓글 수정
	@Transactional
	public void update(ReplyUpdateRequest request, Long cmtId, Long rpId, Member member) {
		Reply findReply = isExistedCommentAndReply(cmtId, rpId);

		hasPermission(member, findReply);

		findReply.update(request);
	}


	// 대댓글 삭제
	@Transactional
	public void delete(Long cmtId, Long rpId, Member member) {
		Reply findReply = isExistedCommentAndReply(cmtId, rpId);

		hasPermission(member, findReply);

		replyRepository.deleteById(rpId);

	}

	private static void hasPermission(Member member, Reply findReply) {
		if (!findReply.getMember().getUsername().equals(member.getUsername())) {
			throw new AccessDeniedException();
		}
	}

	private Reply isExistedCommentAndReply(Long cmtId, Long rpId) {
		Comment findComment = commentRepository.findById(cmtId)
				.orElseThrow(NoSuchCommentException::new);

		Reply findReply = replyRepository.findById(rpId)
				.orElseThrow(NoSuchReplyException::new);
		return findReply;
	}

}

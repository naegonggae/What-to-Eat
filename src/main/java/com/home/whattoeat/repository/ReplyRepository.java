package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Comment;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

	void deleteAllByMember(Member member);
	Page<Reply> findAllByComment(Comment comment, Pageable pageable);

}

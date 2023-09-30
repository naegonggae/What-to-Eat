package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Comment;
import com.home.whattoeat.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

	Page<Reply> findAllByComment(Comment findComment, Pageable pageable);

}

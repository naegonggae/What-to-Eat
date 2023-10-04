package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Comment;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	void deleteAllByMember(Member member);
	Page<Comment> findAllByReview(Review review, Pageable pageable);
}

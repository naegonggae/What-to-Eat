package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	void deleteAllByMember(Member member);
	Page<Review> findAllByRestaurant(Restaurant restaurant, Pageable pageable);

}

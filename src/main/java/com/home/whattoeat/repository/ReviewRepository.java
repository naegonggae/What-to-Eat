package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	Page<Review> findAllByRestaurant(Restaurant restaurant, Pageable pageable);

}

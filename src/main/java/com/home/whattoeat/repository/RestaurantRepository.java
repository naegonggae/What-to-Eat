package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	boolean existsByName(String name);
	Page<Restaurant> findAllByMember(Member member, Pageable pageable);

}

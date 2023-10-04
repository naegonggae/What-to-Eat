package com.home.whattoeat.repository.restaurant;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Restaurant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantRepositoryCustom {

	boolean existsByName(String name);
	Optional<Restaurant> findByName(String name);
	Page<Restaurant> findAllByMember(Member member, Pageable pageable);
	boolean existsByMember(Member member);
}

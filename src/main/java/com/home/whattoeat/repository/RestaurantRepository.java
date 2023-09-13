package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	boolean existsByName(String name);

}

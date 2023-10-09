package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.domain.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {

	boolean existsByRestaurant(Restaurant restaurant);

	void deleteAllByRestaurant(Restaurant restaurant);
}

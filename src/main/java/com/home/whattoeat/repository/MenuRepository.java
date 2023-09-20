package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Menu;
import com.home.whattoeat.domain.Restaurant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	Optional<Menu> findByName(String name);
	Page<Menu> findAllByRestaurant(Restaurant restaurant, Pageable pageable);

}

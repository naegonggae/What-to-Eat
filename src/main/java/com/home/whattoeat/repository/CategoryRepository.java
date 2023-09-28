package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String categoryName);
	boolean existsByName(String categoryName);
}

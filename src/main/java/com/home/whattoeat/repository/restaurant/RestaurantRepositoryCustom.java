package com.home.whattoeat.repository.restaurant;

import com.home.whattoeat.domain.Order;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.dto.restuarant.RstCategoryCondition;
import com.home.whattoeat.dto.restuarant.RstSearchCondition;
import com.home.whattoeat.dto.restuarant.RstSearchKeyword;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {

	Page<Restaurant> searchRstByCategory(RstCategoryCondition condition, Pageable pageable);
	Page<Restaurant> searchRstByCondition(RstSearchCondition condition, Pageable pageable);
	Page<Restaurant> searchRstByKeyword(RstSearchKeyword condition, Pageable pageable);

	List<Order> findAllOrder();

	void bulkDelete(Long id);

}

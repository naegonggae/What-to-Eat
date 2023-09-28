package com.home.whattoeat.repository.restaurant;

import com.home.whattoeat.dto.restuarant.RstCategoryCondition;
import com.home.whattoeat.dto.restuarant.RestaurantCategoryDto;
import com.home.whattoeat.dto.restuarant.RstSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {
	Page<RestaurantCategoryDto> searchRstByCategory(RstCategoryCondition condition, Pageable pageable);
	Page<RestaurantCategoryDto> searchRstByCondition(RstSearchCondition condition, Pageable pageable);

}

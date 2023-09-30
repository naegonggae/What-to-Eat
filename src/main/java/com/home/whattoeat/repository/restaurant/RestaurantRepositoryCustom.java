package com.home.whattoeat.repository.restaurant;

import com.home.whattoeat.dto.restuarant.RstCategoryCondition;
import com.home.whattoeat.dto.restuarant.RestaurantCategoryDto;
import com.home.whattoeat.dto.restuarant.RstFindAllResponse;
import com.home.whattoeat.dto.restuarant.RstSearchCondition;
import com.home.whattoeat.dto.restuarant.RstSearchKeyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {
	Page<RestaurantCategoryDto> searchRstByCategory(RstCategoryCondition condition, Pageable pageable);
	Page<RestaurantCategoryDto> searchRstByCondition(RstSearchCondition condition, Pageable pageable);
	Page<RestaurantCategoryDto> searchRstByKeyword(RstSearchKeyword condition, Pageable pageable);

}

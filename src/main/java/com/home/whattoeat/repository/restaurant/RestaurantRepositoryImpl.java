package com.home.whattoeat.repository.restaurant;

import static com.home.whattoeat.domain.QCategory.category;
import static com.home.whattoeat.domain.QRestaurant.restaurant;
import static com.home.whattoeat.domain.QRestaurantCategory.restaurantCategory;
import static org.springframework.util.StringUtils.hasText;

import com.home.whattoeat.dto.restuarant.QRestaurantCategoryDto;
import com.home.whattoeat.dto.restuarant.RstCategoryCondition;
import com.home.whattoeat.dto.restuarant.RestaurantCategoryDto;
import com.home.whattoeat.dto.restuarant.RstSearchCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

// 클래스 이름은 무조건 RestaurantRepository + Impl 해야함 -> 규칙임
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public RestaurantRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<RestaurantCategoryDto> searchRstByCategory(RstCategoryCondition condition, Pageable pageable) {
		List<RestaurantCategoryDto> content = queryFactory
				.select(new QRestaurantCategoryDto(
						restaurant.name,
						restaurant.phoneNumber,
						restaurant.starRating,
						restaurant.numberOfOrders,
						restaurant.minOrderAmount,
						restaurant.maxOrderAmount
				))
				.from(restaurant)
				.join(restaurant.restaurantCategories, restaurantCategory)
				.join(restaurantCategory.category, category)
				.where(categoryNameEq(condition.getCategoryName()))
				.offset(pageable.getOffset()) // 몇개를 넘기고 가져올건가
				.limit(pageable.getPageSize()) // 몇개씩 가져올건가
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.select(restaurant.count())
				.from(restaurant)
				.join(restaurant.restaurantCategories, restaurantCategory)
				.join(restaurantCategory.category, category)
				.where(categoryNameEq(condition.getCategoryName()));

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<RestaurantCategoryDto> searchRstByCondition(RstSearchCondition condition, Pageable pageable) {

		// 기본 정렬 방식을 아이디(ID) 오름차순으로 설정
		OrderSpecifier<?> orderSpecifier = restaurant.id.asc();

		if ("starRating".equals(condition.getSortBy())) {
			orderSpecifier = restaurant.starRating.desc();
		} else if ("numberOfOrders".equals(condition.getSortBy())) {
			orderSpecifier = restaurant.numberOfOrders.desc();
		}

		List<RestaurantCategoryDto> content = queryFactory
				.select(new QRestaurantCategoryDto(
						restaurant.name,
						restaurant.phoneNumber,
						restaurant.starRating,
						restaurant.numberOfOrders,
						restaurant.minOrderAmount,
						restaurant.maxOrderAmount
				))
				.from(restaurant)
				.join(restaurant.restaurantCategories, restaurantCategory)
				.join(restaurantCategory.category, category)
				.where(
						categoryNameEq(condition.getCategoryName()),
						starRatingGoeEq(condition.getStarRating()),
						minOrderAmountGoeEq(condition.getMinOrderAmount()),
						maxOrderAmountGoeEq(condition.getMaxOrderAmount())
				)
				.orderBy(orderSpecifier)
				.offset(pageable.getOffset()) // 몇개를 넘기고 가져올건가
				.limit(pageable.getPageSize()) // 몇개씩 가져올건가
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.select(restaurant.count())
				.from(restaurant)
				.join(restaurant.restaurantCategories, restaurantCategory)
				.join(restaurantCategory.category, category)
				.where(
						categoryNameEq(condition.getCategoryName()),
						starRatingGoeEq(condition.getStarRating()),
						minOrderAmountGoeEq(condition.getMinOrderAmount()),
						maxOrderAmountGoeEq(condition.getMaxOrderAmount())
				);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}


	// 카테고리 이름에 속한 식당들
	private BooleanExpression categoryNameEq(String categoryName) {
		return hasText(categoryName) ? category.name.eq(categoryName) : null;
	}

	// 별점 몇 이상인 식당들
	private BooleanExpression starRatingGoeEq(Double starRatingGoe) {
		return starRatingGoe != null ? restaurant.starRating.goe(starRatingGoe) : null;
	}

	// 최소주문금액 몇 이상인 식당들
	private BooleanExpression minOrderAmountGoeEq(Integer minOAGoe) {
		return minOAGoe != null ? restaurant.minOrderAmount.goe(minOAGoe) : null;
	}

	// 최대주문금액 몇 이상인 식당들
	private BooleanExpression maxOrderAmountGoeEq(Integer maxOAGoe) {
		return maxOAGoe != null ? restaurant.maxOrderAmount.goe(maxOAGoe) : null;
	}


}

package com.home.whattoeat.repository.restaurant;

import static com.home.whattoeat.domain.QCategory.category;
import static com.home.whattoeat.domain.QRestaurant.restaurant;
import static com.home.whattoeat.domain.QRestaurantCategory.restaurantCategory;
import static org.springframework.util.StringUtils.hasText;

import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.dto.restuarant.RstCategoryCondition;
import com.home.whattoeat.dto.restuarant.RstSearchCondition;
import com.home.whattoeat.dto.restuarant.RstSearchKeyword;
import com.home.whattoeat.exception.reataurant.NoSuchKeywordException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

// 클래스 이름은 무조건 RestaurantRepository + Impl 해야함 -> 규칙임
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public RestaurantRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Page<Restaurant> searchRstByCategory(RstCategoryCondition condition, Pageable pageable) {
		// condition 이 null 이면 전체를 끌어오니 null 처리 필요
		if (condition.getCategoryName().isEmpty()) throw new NoSuchKeywordException();

		pageable = PageRequest.of(0, 8);
		List<Restaurant> content = queryFactory
				.selectDistinct(restaurant)
				.from(restaurant)
				.join(restaurant.restaurantCategoryList, restaurantCategory)
				.join(restaurantCategory.category, category)
				.where(categoryNameEq(condition.getCategoryName()))
				.offset(pageable.getOffset()) // 몇개를 넘기고 가져올건가
				.limit(pageable.getPageSize()) // 몇개씩 가져올건가
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.selectDistinct(restaurant.name.count())
				.from(restaurant)
				.join(restaurant.restaurantCategoryList, restaurantCategory)
				.join(restaurantCategory.category, category)
				.where(categoryNameEq(condition.getCategoryName()));

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<Restaurant> searchRstByCondition(RstSearchCondition condition, Pageable pageable) {

		if (condition.getCategoryName().isEmpty() && condition.getSortBy().isEmpty() &&
				condition.getStarRating() == null && condition.getMinOrderAmount()==null &&
				condition.getMaxOrderAmount()==null) throw new NoSuchKeywordException();

		pageable = PageRequest.of(0, 8);
		List<Restaurant> content = queryFactory
				.selectDistinct(restaurant)
				.from(restaurant)
				.join(restaurant.restaurantCategoryList, restaurantCategory)
				.join(restaurantCategory.category, category)
				.where(
						categoryNameEq(condition.getCategoryName()),
						starRatingGoeEq(condition.getStarRating()),
						minOrderAmountGoeEq(condition.getMinOrderAmount()),
						maxOrderAmountGoeEq(condition.getMaxOrderAmount())
				)
				.orderBy(getOrderSpecifier(condition))
				.offset(pageable.getOffset()) // 몇개를 넘기고 가져올건가
				.limit(pageable.getPageSize()) // 몇개씩 가져올건가
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.selectDistinct(restaurant.name.count())
				.from(restaurant)
				.join(restaurant.restaurantCategoryList, restaurantCategory)
				.join(restaurantCategory.category, category)
				.where(
						categoryNameEq(condition.getCategoryName()),
						starRatingGoeEq(condition.getStarRating()),
						minOrderAmountGoeEq(condition.getMinOrderAmount()),
						maxOrderAmountGoeEq(condition.getMaxOrderAmount())
				);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	private static OrderSpecifier<?> getOrderSpecifier(RstSearchCondition condition) {
		OrderSpecifier<?> orderSpecifier = restaurant.reviewCount.asc();

		if ("starRating".equals(condition.getSortBy())) {
			orderSpecifier = restaurant.starRating.desc();
		} else if ("numberOfOrders".equals(condition.getSortBy())) {
			orderSpecifier = restaurant.numberOfOrders.desc();
		} else if ("reviewCount".equals(condition.getSortBy())) {
			orderSpecifier = restaurant.reviewCount.desc();
		}
		return orderSpecifier;
	}

	@Override
	public Page<Restaurant> searchRstByKeyword(RstSearchKeyword condition,
			Pageable pageable) {

		if(condition.getKeyword().isEmpty()) throw new NoSuchKeywordException();
		pageable = PageRequest.of(0, 8);

		List<Restaurant> content = queryFactory
				.selectDistinct(restaurant)
				.from(restaurant)
				.join(restaurant.restaurantCategoryList, restaurantCategory)
				.join(restaurantCategory.category, category)
				.where(
						categoryNameEq(condition.getKeyword())
						.or(rstNameContainsEq(condition.getKeyword()))
				)
				.offset(pageable.getOffset()) // 몇개를 넘기고 가져올건가
				.limit(pageable.getPageSize()) // 몇개씩 가져올건가
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.selectDistinct(restaurant.name.count())
				.from(restaurant)
				.join(restaurant.restaurantCategoryList, restaurantCategory)
				.join(restaurantCategory.category, category)
				.where(
						categoryNameEq(condition.getKeyword())
						.or(rstNameContainsEq(condition.getKeyword()))
				);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	// 식당이름에 키워드가 포함되어 있는 식당들
	private BooleanExpression rstNameContainsEq(String keyword) {
		return hasText(keyword) ? restaurant.name.contains(keyword) : null;
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

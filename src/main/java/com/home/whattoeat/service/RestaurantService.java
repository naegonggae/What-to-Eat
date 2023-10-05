package com.home.whattoeat.service;

import com.home.whattoeat.domain.Category;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Menu;
import com.home.whattoeat.domain.Order;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.domain.RestaurantCategory;
import com.home.whattoeat.dto.restuarant.RstCategoryCondition;
import com.home.whattoeat.dto.restuarant.RstFindResponse;
import com.home.whattoeat.dto.restuarant.RstSaveRequest;
import com.home.whattoeat.dto.restuarant.RstSaveResponse;
import com.home.whattoeat.dto.restuarant.RstSearchCondition;
import com.home.whattoeat.dto.restuarant.RstSearchKeyword;
import com.home.whattoeat.dto.restuarant.RstUpdateRequest;
import com.home.whattoeat.exception.category.NoSuchCategoryException;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.reataurant.DuplicateRestaurantException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import com.home.whattoeat.repository.CategoryRepository;
import com.home.whattoeat.repository.MenuRepository;
import com.home.whattoeat.repository.order.OrderRepository;
import com.home.whattoeat.repository.restaurant.RestaurantRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

	private final CategoryRepository categoryRepository;
	private final RestaurantRepository restaurantRepository;
	private final MenuRepository menuRepository;
	private final OrderRepository orderRepository;

	// 식당 등록
	@Transactional
	public RstSaveResponse save(RstSaveRequest request, Member member) {

		// 식당이름 중복 확인
		boolean existsByName = restaurantRepository.existsByName(request.getName());
		if (existsByName) throw new DuplicateRestaurantException();

		// 카테고리 String 리스트 -> RestaurantCategory 로 변경
		List<RestaurantCategory> restaurantCategoryList = changeStringListToRestaurantCategoryList(
				request.getCategoryNames());

		// 식당 생성
		Restaurant restaurant = Restaurant.createRestaurant(request, member, restaurantCategoryList);

		Restaurant savedRestaurant = restaurantRepository.save(restaurant);
		return RstSaveResponse.from(savedRestaurant);
	}

	// 단건 조회
	public RstFindResponse findOne(Long id) {
		Restaurant findRestaurant = restaurantRepository.findById(id)
				.orElseThrow(NoSuchRestaurantException::new);
		return RstFindResponse.from(findRestaurant);
	}

	// 내가 등록한 식당 전체 조회
	public Page<RstFindResponse> findAllMy(Pageable pageable, Member member) {
		return restaurantRepository.findAllByMember(member, pageable).map(RstFindResponse::from);
	}

	// 카테고리 이름으로 식당 검색
	public Page<RstFindResponse> findAllByCategory(Pageable pageable, RstCategoryCondition request) {
		return restaurantRepository.searchRstByCategory(request, pageable).map(RstFindResponse::from);
	}

	// 카테고리 이름, 정렬기준(별점, 주문 수, 리뷰 수), 별점 몇점 이상, 최소주문금액 몇 이상, 최대주문금액 몇 이상 조건검색
	public Page<RstFindResponse> findAllByCondition(Pageable pageable, RstSearchCondition request) {
		return restaurantRepository.searchRstByCondition(request, pageable).map(RstFindResponse::from);
	}

	// 검색 키워드가 식당이름에 속해있거나 카테고리 이름인 식당 조회
	public Page<RstFindResponse> findAllByKeyword(Pageable pageable, RstSearchKeyword request) {
		return restaurantRepository.searchRstByKeyword(request, pageable).map(RstFindResponse::from);
	}

	// 식당 수정
	@Transactional
	public void update(Long id, RstUpdateRequest request, String username) {
		// 로그인한 유저정보 == 레스토랑 등록한 사람일 경우만 수정 가능
		Restaurant restaurant = hasPermission(id, username);

		// 카테고리 수정
		List<RestaurantCategory> restaurantCategoryList = changeStringListToRestaurantCategoryList(
				request.getCategoryNames());

		restaurant.update(request, restaurantCategoryList);
	}


	// 식당 삭제
	@Transactional
	public void delete(Long id, String username) {
		// 로그인한 유저정보 == 레스토랑 등록한 사람일 경우만 삭제 가능
		Restaurant restaurant = hasPermission(id, username);

		// 주문은 현재 생성시점에서 완료된걸로 취급하기 때문에 진행중인 주문은 없다.

		// 메뉴에 있는 식당을 null 로 변환
		List<Menu> menuList = menuRepository.findAllByRestaurant(restaurant);
		menuList.stream().forEach(menu -> menu.removeRestaurant());

		// 주문에 있는 식당을 null 로 변환
		List<Order> orderList = orderRepository.findAllByRestaurant(restaurant);
		orderList.stream().forEach(order -> order.removeRestaurant(restaurant));

		restaurantRepository.deleteById(id);
	}

	private List<RestaurantCategory> changeStringListToRestaurantCategoryList(List<String> categoryList) {
		List<RestaurantCategory> restaurantCategoryList = categoryList.stream()
				.map(categoryName -> {
					// 사용자가 입력한 카테고리 이름이 등록된 카테고리인지 확인
					Category findCategory = categoryRepository.findByName(categoryName)
							.orElseThrow(NoSuchCategoryException::new);
					return RestaurantCategory.createCategory(findCategory);
				})
				.collect(Collectors.toList());
		return restaurantCategoryList;
	}

	private Restaurant hasPermission(Long id, String username) {
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(NoSuchRestaurantException::new);
		if (!username.equals(restaurant.getMember().getUsername())) throw new AccessDeniedException();
		return restaurant;
	}

}

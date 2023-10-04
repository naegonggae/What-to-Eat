package com.home.whattoeat.service;

import com.home.whattoeat.domain.Category;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.domain.RestaurantCategory;
import com.home.whattoeat.dto.restuarant.RestaurantCategoryDto;
import com.home.whattoeat.dto.restuarant.RstFindAllResponse;
import com.home.whattoeat.dto.restuarant.RstCategoryCondition;
import com.home.whattoeat.dto.restuarant.RstFindOneResponse;
import com.home.whattoeat.dto.restuarant.RstSaveRequest;
import com.home.whattoeat.dto.restuarant.RstSaveResponse;
import com.home.whattoeat.dto.restuarant.RstSearchCondition;
import com.home.whattoeat.dto.restuarant.RstSearchKeyword;
import com.home.whattoeat.dto.restuarant.RstUpdateRequest;
import com.home.whattoeat.exception.category.NoSuchCategoryException;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import com.home.whattoeat.repository.CategoryRepository;
import com.home.whattoeat.repository.MemberRepository;
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
	private final MemberRepository memberRepository;

	// 등록
	@Transactional
	public RstSaveResponse save(RstSaveRequest request, String username) {
		// 시큐리티에 유저정보일텐데 굳이 검사해야함? -> member 저장해야하니까
		Member findMember = memberRepository.findByUsername(username)
				.orElseThrow(NoSuchMemberException::new);
		// 식당이름 중복 확인

		// 카테고리 이름 리스트 받아오기
		List<String> categoryNames = request.getCategoryName();

		// 레스토랑 카테고리 생성
		List<RestaurantCategory> restaurantCategoryList = categoryNames.stream()
				.map(categoryName -> {
					// 카테고리 조회
					Category findCategory = categoryRepository.findByName(categoryName)
							.orElseThrow(NoSuchCategoryException::new);
					// 레스토랑 카테고리 생성
					return RestaurantCategory.createCategory(findCategory);
				})
				// 리스트 변환
				.collect(Collectors.toList());

		// 레스토랑 카테고리 생성
//		RestaurantCategory restaurantCategory = RestaurantCategory.createCategory(categoryList);

		// 식당 생성
		Restaurant restaurant = Restaurant.createRestaurant(request, findMember, restaurantCategoryList);

		Restaurant savedRestaurant = restaurantRepository.save(restaurant);
		return RstSaveResponse.from(savedRestaurant);
	}

	// 단건 조회
	public RstFindOneResponse findOne(Long id, String username) {
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(NoSuchRestaurantException::new);
		if (!username.equals(restaurant.getMember().getUsername())) throw new AccessDeniedException();


		return RstFindOneResponse.from(restaurant);
	}


	// 검색받은 카테고리인 식당 전체 조회
	public Page<RestaurantCategoryDto> findAllByCategory(Pageable pageable, RstCategoryCondition request) {

		System.out.println("====");
		System.out.println(request.getCategoryName());

		return restaurantRepository.searchRstByCategory(request, pageable).map(RestaurantCategoryDto::from);
	}

	// 검색받은 카테고리인 식당에서 조건 검색조회
	public Page<RestaurantCategoryDto> findAllByCondition(Pageable pageable, RstSearchCondition request) {

		System.out.println("====");
		System.out.println(request.getStarRating());

		return restaurantRepository.searchRstByCondition(request, pageable).map(RestaurantCategoryDto::from);
	}

	// 검색한 키워드의 카테고리를 가지고 있거나 식당이름에 키워드가 들어가면 조회
	public Page<RestaurantCategoryDto> findAllByKeyword(RstSearchKeyword request, Pageable pageable) {
		return restaurantRepository.searchRstByKeyword(request, pageable).map(RestaurantCategoryDto::from);
	}

	// 내가 등록한 식당 전체 조회
	public Page<RstFindAllResponse> findAll(Pageable pageable, String username) {
		Member findMember = memberRepository.findByUsername(username)
				.orElseThrow(NoSuchMemberException::new);

		return restaurantRepository.findAllByMember(findMember, pageable).map(RstFindAllResponse::from);
	}

	// 수정
	@Transactional
	public void update(Long id, RstUpdateRequest request, String username) {
		// 로그인한 유저정보 == 레스토랑 등록한 사람일 경우만 수정 가능
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(NoSuchRestaurantException::new);
		if (!username.equals(restaurant.getMember().getUsername())) throw new AccessDeniedException();

		// 카테고리 수정
		List<String> categoryNames = request.getCategoryName();
		List<RestaurantCategory> restaurantCategoryList = categoryNames.stream()
				.map(categoryName -> {
					Category findCategory = categoryRepository.findByName(categoryName)
							.orElseThrow(NoSuchCategoryException::new);
					return RestaurantCategory.createCategory(findCategory);
				})
				.collect(Collectors.toList());

		restaurant.update(request, restaurantCategoryList);
	}

	// 삭제
	@Transactional
	public void delete(Long id, String username) {
		// 로그인한 유저정보 == 레스토랑 등록한 사람일 경우만 삭제 가능
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(NoSuchRestaurantException::new);
		// 레스토랑을 등록한 사람이 로그인한사람이 아닐경우 권한 오류발생
		if (!username.equals(restaurant.getMember().getUsername())) throw new AccessDeniedException();

		// 남은 배송이 있는가? 체크
		restaurant.softDelete();
	}

	// 요약정보 보기? 매출같은거나 주문수 타겟층 분석

}

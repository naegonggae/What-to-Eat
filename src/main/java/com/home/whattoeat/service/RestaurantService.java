package com.home.whattoeat.service;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.dto.restuarant.RstFindAllResponse;
import com.home.whattoeat.dto.restuarant.RstFindOneResponse;
import com.home.whattoeat.dto.restuarant.RstSaveRequest;
import com.home.whattoeat.dto.restuarant.RstSaveResponse;
import com.home.whattoeat.dto.restuarant.RstUpdateRequest;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import com.home.whattoeat.exception.reataurant.NoSuchRstException;
import com.home.whattoeat.repository.MemberRepository;
import com.home.whattoeat.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final MemberRepository memberRepository;

	// 등록
	@Transactional
	public RstSaveResponse save(RstSaveRequest request, String username) {
		// 시큐리티에 유저정보일텐데 굳이 검사해야함?
		// 그냥 존재하는 유저가 있으면 됨
		Member findMember = memberRepository.findByUsername(username)
				.orElseThrow(NoSuchMemberException::new);

		Restaurant restaurant = Restaurant.builder()
				.name(request.getName())
				.phoneNumber(request.getPhoneNumber())
				.cuisineType(request.getCuisineType())
				.member(findMember)
				.build();

		Restaurant savedRst = restaurantRepository.save(restaurant);
		return RstSaveResponse.from(savedRst);
	}

	// 단건 조회
	public RstFindOneResponse findOne(Long id) {
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(NoSuchRstException::new);

		return RstFindOneResponse.from(restaurant);
	}

	// 전체 조회
	public Page<RstFindAllResponse> findAll(Pageable pageable) {
		return restaurantRepository.findAll(pageable).map(RstFindAllResponse::from);
	}

	// 수정
	@Transactional
	public void update(Long id, RstUpdateRequest request) {
		// 로그인한 유저정보 == 레스토랑 등록한 사람일 경우만 수정 가능
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(NoSuchRstException::new);

		restaurant.update(request);
	}

	// 삭제
	@Transactional
	public void delete(Long id) {
		// 로그인한 유저정보 == 레스토랑 등록한 사람일 경우만 삭제 가능
		Restaurant restaurant = restaurantRepository.findById(id)
				.orElseThrow(NoSuchRstException::new);
		// 남은 배송이 있는가? 체크
		restaurant.softDelete();
	}

	// 요약정보 보기? 매출같은거나 주문수 타겟층 분석

}

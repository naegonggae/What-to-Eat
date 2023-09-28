package com.home.whattoeat.service;

import com.home.whattoeat.domain.Menu;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.dto.menu.MenuSaveRequest;
import com.home.whattoeat.dto.menu.MenuSaveResponse;
import com.home.whattoeat.dto.menu.MenuFindResponse;
import com.home.whattoeat.dto.menu.MenuUpdateRequest;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.menu.NoSuchMenuException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import com.home.whattoeat.repository.MenuRepository;
import com.home.whattoeat.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

	private final MenuRepository menuRepository;
	private final RestaurantRepository restaurantRepository;

	// 메뉴 등록
	@Transactional
	public MenuSaveResponse save(MenuSaveRequest request, Long id, String username) {
		// 시큐리티에 유저정보일텐데 굳이 검사해야함? ㅇㅇ 안해도 됨
//		Member findMember = memberRepository.findByUsername(username)
//				.orElseThrow(NoSuchMemberException::new);

		Restaurant findRestaurant = restaurantRepository.findById(id)
				.orElseThrow(NoSuchRestaurantException::new);

		// 명시된 레스토랑 아이디로 식당 조회 -> 그 식당이 내가 만든 식당인지 확인
		if (!username.equals(findRestaurant.getMember().getUsername())) throw new AccessDeniedException();

		// 내가 만든걸로 검증된 식당을 새로운 메뉴 등록할때 사용
		Menu menu = Menu.builder()
				.name(request.getName())
				.description(request.getDescription())
				.price(request.getPrice())
				.restaurant(findRestaurant)
				.build();

		Menu savedMenu = menuRepository.save(menu);
		return MenuSaveResponse.from(savedMenu);
	}
	// 내가 등록한 식당의 메뉴 단건 조회
	public MenuFindResponse findOne(Long rstId, Long menuId, String username) {

		Restaurant findRestaurant = restaurantRepository.findById(rstId)
				.orElseThrow(NoSuchRestaurantException::new);

		// 명시된 레스토랑 아이디로 식당 조회 -> 그 식당이 내가 만든 식당인지 확인
		if (!username.equals(findRestaurant.getMember().getUsername())) throw new AccessDeniedException();

		Menu findMenu = menuRepository.findById(menuId)
				.orElseThrow(NoSuchMenuException::new);

		// 명시된 메뉴 아이디로 메뉴를 조회 -> 그 메뉴가 속한 식당이 내가 만든 식당인지 확인
		if (!findMenu.getRestaurant().equals(findRestaurant)) throw new AccessDeniedException();

		return MenuFindResponse.from(findMenu);
	}

	// 내가 등록한 식당 메뉴 전체 조회
	public Page<MenuFindResponse> findAll(Long rstId, Pageable pageable, String username) {
		Restaurant findRestaurant = restaurantRepository.findById(rstId)
				.orElseThrow(NoSuchRestaurantException::new);

		// 명시된 레스토랑 아이디로 식당 조회 -> 그 식당이 내가 만든 식당인지 확인
		if (!username.equals(findRestaurant.getMember().getUsername())) throw new AccessDeniedException();

		// 내가 만든 식당의 메뉴를 전부가져오는 findAllByRestaurant 사용
		return menuRepository.findAllByRestaurant(findRestaurant, pageable).map(MenuFindResponse::from);
	}

	// 메뉴 수정
	@Transactional
	public void update(MenuUpdateRequest request, Long rstId, Long menuId, String username) {

		Restaurant findRestaurant = restaurantRepository.findById(rstId)
				.orElseThrow(NoSuchRestaurantException::new);

		// 명시된 레스토랑 아이디로 식당 조회 -> 그 식당이 내가 만든 식당인지 확인
		if (!username.equals(findRestaurant.getMember().getUsername())) throw new AccessDeniedException();

		Menu findMenu = menuRepository.findById(menuId)
				.orElseThrow(NoSuchMenuException::new);

		// 명시된 메뉴 아이디로 메뉴를 조회 -> 그 메뉴가 속한 식당이 내가 만든 식당인지 확인
		if (!findMenu.getRestaurant().equals(findRestaurant)) throw new AccessDeniedException();

		findMenu.update(request);
	}

	// 메뉴 삭제
	@Transactional
	public void delete(Long rstId, Long menuId, String username) {
		Restaurant findRestaurant = restaurantRepository.findById(rstId)
				.orElseThrow(NoSuchRestaurantException::new);

		// 명시된 레스토랑 아이디로 식당 조회 -> 그 식당이 내가 만든 식당인지 확인
		if (!username.equals(findRestaurant.getMember().getUsername())) throw new AccessDeniedException();

		Menu findMenu = menuRepository.findById(menuId)
				.orElseThrow(NoSuchMenuException::new);

		// 명시된 메뉴 아이디로 메뉴를 조회 -> 그 메뉴가 속한 식당이 내가 만든 식당인지 확인
		if (!findMenu.getRestaurant().equals(findRestaurant)) throw new AccessDeniedException();

		// 이걸 주문한 손님은 없나 확인
//		menuRepository.delete(findMenu);
		findMenu.softDelete();
	}


}

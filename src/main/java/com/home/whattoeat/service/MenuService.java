package com.home.whattoeat.service;

import com.home.whattoeat.domain.Member;
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
	public MenuSaveResponse save(MenuSaveRequest request, Long id, Member member) {

		Restaurant findRestaurant = findRestaurant(id);

		// 메뉴등록은 식당을 만든 사람만 가능
		hasPermission(member.getUsername().equals(findRestaurant.getMember().getUsername()));

		// 내가 만든걸로 검증된 식당을 새로운 메뉴 등록할때 사용
		Menu menu = Menu.createMenu(request, findRestaurant);

		Menu savedMenu = menuRepository.save(menu);
		return MenuSaveResponse.from(savedMenu);
	}
	// 이용 x - 메뉴 단건 조회
	public MenuFindResponse findOne(Long rstId, Long menuId) {

		Restaurant findRestaurant = findRestaurant(rstId);

		Menu findMenu = findMenu(menuId);

		// 메뉴에 저장된 식당이 파라미터로 받아온 식당인지
		isSameRestaurant(findMenu.getRestaurant().equals(findRestaurant));

		return MenuFindResponse.from(findMenu);
	}

	// 식당 메뉴 전체 조회
	public Page<MenuFindResponse> findAll(Long rstId, Pageable pageable) {
		Restaurant findRestaurant = findRestaurant(rstId);
		return menuRepository.findAllByRestaurant(findRestaurant, pageable).map(MenuFindResponse::from);
	}

	// 메뉴 수정
	@Transactional
	public void update(MenuUpdateRequest request, Long rstId, Long menuId, Member member) {

		Restaurant findRestaurant = findRestaurant(rstId);

		Menu findMenu = findMenu(menuId);

		// 메뉴 수정은 식당을 만든 사람만 가능
		hasPermission(member.getUsername().equals(findRestaurant.getMember().getUsername()));

		// 메뉴에 저장된 식당과 파라미터로 받아온 식당이 같은지 확인
		isSameRestaurant(findMenu.getRestaurant().equals(findRestaurant));

		findMenu.update(request);
	}

	// 메뉴 삭제
	@Transactional
	public void delete(Long rstId, Long menuId, Member member) {
		Restaurant findRestaurant = findRestaurant(rstId);

		Menu findMenu = findMenu(menuId);

		// 메뉴 삭제는 식당을 만든 사람만 가능
		hasPermission(member.getUsername().equals(findRestaurant.getMember().getUsername()));

		// 메뉴에 저장된 식당과 파라미터로 받아온 식당이 같은지 확인
		isSameRestaurant(findMenu.getRestaurant().equals(findRestaurant));

		menuRepository.deleteById(menuId);
	}

	private Menu findMenu(Long menuId) {
		Menu findMenu = menuRepository.findById(menuId)
				.orElseThrow(NoSuchMenuException::new);
		return findMenu;
	}

	private Restaurant findRestaurant(Long rstId) {
		Restaurant findRestaurant = restaurantRepository.findById(rstId)
				.orElseThrow(NoSuchRestaurantException::new);
		return findRestaurant;
	}

	private static void isSameRestaurant(boolean findMenu) {
		if (!findMenu) throw new AccessDeniedException();
	}

	private static void hasPermission(boolean member) {
		isSameRestaurant(member);
	}

}

package com.home.whattoeat.service;

import com.home.whattoeat.domain.Delivery;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Menu;
import com.home.whattoeat.domain.Order;
import com.home.whattoeat.domain.OrderMenu;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.dto.order.OrderFindResponse;
import com.home.whattoeat.dto.order.OrderSaveRequest;
import com.home.whattoeat.dto.order.OrderSaveResponse;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.menu.NoSuchMenuException;
import com.home.whattoeat.exception.order.NoSuchOrderException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import com.home.whattoeat.repository.MemberRepository;
import com.home.whattoeat.repository.MenuRepository;
import com.home.whattoeat.repository.OrderRepository;
import com.home.whattoeat.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final MenuRepository menuRepository;
	private final RestaurantRepository restaurantRepository;
	private final MemberRepository memberRepository;

	// 주문 - 결제 - 배송
	@Transactional
	public OrderSaveResponse order(OrderSaveRequest request, Member member) {

		// 내가 만든것들이 아니여도 상관없음
		Restaurant findRestaurant = restaurantRepository.findByName(request.getRestaurantName())
				.orElseThrow(NoSuchRestaurantException::new);

//		if (!member.equals(findRestaurant.getMember())) throw new AccessDeniedException();
		Menu findMenu = menuRepository.findByName(request.getMenuName())
				.orElseThrow(NoSuchMenuException::new);

//		if (!findRestaurant.getMember().equals(findMenu.getRestaurant())) throw new AccessDeniedException();

		OrderMenu orderMenu = OrderMenu.createMenu(findMenu, request.getQuantity(), findMenu.getPrice());
		Delivery delivery = new Delivery();
		delivery.changeStatus();
		Order order = Order.createOrder(member, delivery, orderMenu);
		Order savedOrder = orderRepository.save(order);

//		return OrderSaveResponse.from(savedOrder);
		return new OrderSaveResponse(savedOrder);
	}

	// 주문취소 - 환불
	@Transactional
	public void OrderCancel(Long orderId, Member member) {
		Order findOrder = orderRepository.findById(orderId)
				.orElseThrow(NoSuchOrderException::new);

		System.out.println("member : " + member);
		System.out.println("findOrder.getMember() : " + findOrder.getMember());
		if (!member.getUsername().equals(findOrder.getMember().getUsername())) throw new AccessDeniedException();

		findOrder.orderCancel();
		// 이게 바로 삭제할일인지 판단해봐
		findOrder.softDelete();
	}

	// 내가 한 주문 조회
	public Page<OrderFindResponse> findAllMyOrder(Pageable pageable, Member member) {
		return orderRepository.findAllByMember(member, pageable).map(OrderFindResponse::from);
	}

	// 내가 한 주문 단건 조회
	public OrderFindResponse findOne(Long orderId, Member member) {
		Order findOrder = orderRepository.findById(orderId)
				.orElseThrow(NoSuchOrderException::new);
		System.out.println("findOne 시작");
		System.out.println("member.getUsername() : "+member);
		System.out.println("findOne 중간사이");

		System.out.println("findOrder.getMember().getUsername() : "+findOrder.getMember()); // order 에 제때 member 가 반영이 안되나?
		System.out.println("findOne 에러나기 직전");


		if (!member.getUsername().equals(findOrder.getMember().getUsername())) throw new AccessDeniedException();
		return OrderFindResponse.from(findOrder);
	}
}

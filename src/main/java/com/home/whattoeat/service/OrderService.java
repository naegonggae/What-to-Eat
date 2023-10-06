package com.home.whattoeat.service;

import com.home.whattoeat.domain.Cart;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Order;
import com.home.whattoeat.dto.order.OrderFindResponse;
import com.home.whattoeat.dto.order.OrderSaveRequest;
import com.home.whattoeat.dto.order.OrderSaveResponse;
import com.home.whattoeat.exception.cart.NoSuchCartException;
import com.home.whattoeat.exception.member.AccessDeniedException;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import com.home.whattoeat.exception.order.NoSuchOrderException;
import com.home.whattoeat.repository.CartRepository;
import com.home.whattoeat.repository.MemberRepository;
import com.home.whattoeat.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final CartRepository cartRepository;

	// 장바구니는 만들어져 있고 주문 버튼 누르기
	@Transactional
	public OrderSaveResponse order(OrderSaveRequest request, String username) {

		Member member = memberRepository.findByUsername(username)
				.orElseThrow(NoSuchMemberException::new);

		// 장바구니가 존재하는지 확인
		Cart findCart = cartRepository.findByMember(member)
				.orElseThrow(NoSuchCartException::new);

		Order order = Order.createOrder(request, member, findCart);

		// 장바구니 비우기
		cartRepository.deleteById(findCart.getId());

		Order savedOrder = orderRepository.save(order);
		return OrderSaveResponse.from(savedOrder);
	}

	// 주문취소
	@Transactional
	public void OrderCancel(Long orderId, Member member) {
		Order findOrder = orderRepository.findById(orderId)
				.orElseThrow(NoSuchOrderException::new);

		hasPermission(member, findOrder);

		findOrder.orderCancel();
//		orderRepository.deleteById(orderId);
	}

	// 내가 한 주문 조회
	public Page<OrderFindResponse> findAllMyOrder(Pageable pageable, Member member) {
		return orderRepository.findAllOrder(member.getUsername(), pageable).map(OrderFindResponse::from);
	}

	// 내가 한 주문 단건 조회
	public OrderFindResponse findOne(Long orderId, Member member) {
		Order findOrder = orderRepository.findById(orderId)
				.orElseThrow(NoSuchOrderException::new);

		hasPermission(member, findOrder);
		return OrderFindResponse.from(findOrder);
	}

	private static void hasPermission(Member member, Order findOrder) {
		if (!member.getUsername().equals(findOrder.getMember().getUsername())) throw new AccessDeniedException();
	}

}

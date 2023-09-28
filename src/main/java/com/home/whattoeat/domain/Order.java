package com.home.whattoeat.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Builder
@Table(name = "orders")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Order extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id") // Order DB에 생성될 member_id 와 연결
	private Member member;
//	@Column(name = "restaurant_id")
//	private Restaurant restaurant;
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderMenu> orderMenuList = new ArrayList<>();

	private String orderTime;
	private String totalAmount;

	// 배달할 주소 적어야함
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; // ORDER, CANCEL

	private LocalDateTime orderDate; // 주문시간

	// 배달주소
	// 주문상태
	// 배달원
	// 결제정보
	// 주문 특이사항
	// 배달 요청시간

	// 연관관계 메서드
	public void addMember(Member member) {
		this.member = member; // Order (여기에 있는 클래스) 에 member 를 파라미터에 받은 member 로 설정해준다.
		member.getOrderList().add(this); // 파라미터로 받은 member 에 있는 orderList 에 여기 this = Order 을 업데이트 시킨다.
	}
	public void addOrderDetail(OrderMenu orderMenu) {
		this.orderMenuList.add(orderMenu); // 여기 orderMenuList 에 orderMenu 채워줘
		orderMenu.addOrder(this); // orderMenu 에 있는 order this = 요 클래스로 채워줘
	}


	// 주문 생성
	public static Order createOrder(Member member, OrderMenu... orderMenus) { // ... 으로 list 넘김
		Order order = new Order();
		order.makeOrder(member, orderMenus);
		return order;
	}
	public void makeOrder(Member member, OrderMenu... orderMenus) {
		this.member = member;
		for (OrderMenu orderMenu : orderMenus) {
			this.addOrderDetail(orderMenu);
		}
		this.orderStatus = OrderStatus.ORDER;
		this.orderDate = LocalDateTime.now();
	}

	// 주문 취소
	public void orderCancel() {
		System.out.println("주문취소 로직에 도착했습니다.");

		this.orderStatus = OrderStatus.CANCEL;
		for (OrderMenu orderMenu : orderMenuList) {
			orderMenu.cancel();
		}
	}

	// 전체 주문 가격 조회
	public int getTotalPrice() {
		int totalPrice = 0;
		for (OrderMenu orderMenu : orderMenuList) {
			totalPrice += orderMenu.getTotalPrice();
		}
		return totalPrice;
	}

}

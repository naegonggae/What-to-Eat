package com.home.whattoeat.domain;

import static lombok.AccessLevel.PROTECTED;

import com.home.whattoeat.dto.order.OrderSaveRequest;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Order extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;
	private String orderNotes;
	private Integer totalAmount;
	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status; // ORDER, CANCEL
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderMenu> orderMenuList = new ArrayList<>();

	// 연관관계 메서드 //
	public void addMember(Member member) {
		this.member = member;
		member.getOrderList().add(this);
	}
	public void addOrderDetail(OrderMenu orderMenu) {
		this.orderMenuList.add(orderMenu);
		orderMenu.addOrder(this);
	}

	// 생성 메서드 //
	public static Order createOrder(OrderSaveRequest request, Member member, Cart cart) {
		return new Order(request, member, cart);
	}
	public Order(OrderSaveRequest request, Member member, Cart cart) {
		this.orderNotes = request.getOrderNotes();
		this.totalAmount = getTotalAmount();
		this.status = OrderStatus.ORDER;
		this.orderDate = LocalDateTime.now();
		addMember(member);
		this.restaurant = cart.getCartMenus().get(0).getMenu().getRestaurant();
		this.restaurant.increaseOrderCount(); // 확인 필요
		copyCartMenuToOrderMenu(cart).stream()
				.forEach(oderMenu -> this.addOrderDetail(oderMenu));
	}
	// CartMenu -> OrderMenu 로 복사
	private static List<OrderMenu> copyCartMenuToOrderMenu(Cart cart) {
		List<OrderMenu> copiedOrderMenu = new ArrayList<>();
		for (CartMenu cartMenu : cart.getCartMenus()) {
			copiedOrderMenu.add(OrderMenu.createMenu(cartMenu));
		}
		return copiedOrderMenu;
	}

	// 비즈니스 메서드 //
	// 주문 취소
	public void orderCancel() {
		this.status = OrderStatus.CANCEL;
	}

	// 전체 주문 가격 조회
	public Integer getTotalAmount() {
		Integer totalPrice = 0;
		for (OrderMenu orderMenu : orderMenuList) {
			totalPrice += orderMenu.getTotalPrice();
		}
		return totalPrice;
	}

	public void removeRestaurant() {
		this.restaurant = null;
	}

	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", orderNotes='" + orderNotes + '\'' +
				", totalAmount=" + totalAmount +
				", orderDate=" + orderDate +
				", status=" + status +
				'}';
	}

}

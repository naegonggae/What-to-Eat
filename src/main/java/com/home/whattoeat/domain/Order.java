package com.home.whattoeat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
public class Order {

	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
//	@Column(name = "restaurant_id")
//	private Restaurant restaurant;
	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetailList = new ArrayList<>();
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Delivery delivery;
	private String orderTime;
	private String totalAmount;


}

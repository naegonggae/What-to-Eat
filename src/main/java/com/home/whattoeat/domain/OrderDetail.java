package com.home.whattoeat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class OrderDetail extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "orderDetail_id")
	private Long id;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;
	private String quantity;

}

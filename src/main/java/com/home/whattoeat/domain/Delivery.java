package com.home.whattoeat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Delivery extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "deliver_id")
	private Long id;
	@JsonIgnore
	@OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
	private Order order;
	@Embedded
	private Address address;
	@Enumerated(EnumType.STRING)
	private DeliveryStatus deliveryStatus;

}

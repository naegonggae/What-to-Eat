package com.home.whattoeat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class DeliveryDriver {

	@Id @GeneratedValue
	@Column(name = "delivery_driver_id")
	private Long id;

	private String name;
	private String phoneNumber;
	private String LicenseNumber;
	@Embedded
	private Address address;
	@Enumerated(EnumType.STRING)
	private DriverStatus driverStatus; // POSSIBLE, READY

}

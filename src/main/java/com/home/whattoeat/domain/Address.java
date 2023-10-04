package com.home.whattoeat.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

	private String city;
	private String street;
	private String zipcode;

	public static Address createAddress(String city, String street, String zipcode) {
		return new Address(city, street, zipcode);
	}
}

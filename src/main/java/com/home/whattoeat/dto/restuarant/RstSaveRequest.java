package com.home.whattoeat.dto.restuarant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RstSaveRequest {

	private String name;
	private String phoneNumber;
	private String cuisineType;

}

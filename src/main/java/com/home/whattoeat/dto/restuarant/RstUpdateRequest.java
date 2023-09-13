package com.home.whattoeat.dto.restuarant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RstUpdateRequest {

	private String name;
	private String phoneNumber;
	private String cuisineType;

}

package com.home.whattoeat.dto.menu;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuSaveRequest {

	private String name;
	private String description;
	private Integer price;

}

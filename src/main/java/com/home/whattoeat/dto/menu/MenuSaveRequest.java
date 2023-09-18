package com.home.whattoeat.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuSaveRequest {

	private String name;
	private String description;
	private int price;

}

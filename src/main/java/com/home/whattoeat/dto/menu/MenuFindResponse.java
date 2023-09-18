package com.home.whattoeat.dto.menu;

import com.home.whattoeat.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuFindResponse {

	private Long id;

	public static MenuFindResponse from(Menu menu) {
		return new MenuFindResponse(menu.getId());
	}

}

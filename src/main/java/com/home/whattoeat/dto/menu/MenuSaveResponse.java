package com.home.whattoeat.dto.menu;

import com.home.whattoeat.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuSaveResponse {

	private Long id;

	public static MenuSaveResponse from(Menu menu) {
		return new MenuSaveResponse(menu.getId());
	}
}

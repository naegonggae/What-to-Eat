package com.home.whattoeat.dto.menu;

import com.home.whattoeat.domain.Menu;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuSaveResponse {

	private Long id;
	private String name;
	private String description;
	private Integer price;

	public static MenuSaveResponse from(Menu menu) {
		return new MenuSaveResponse(
				menu.getId(),
				menu.getName(),
				menu.getDescription(),
				menu.getPrice());
	}
}

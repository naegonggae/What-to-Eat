package com.home.whattoeat.controller.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class restaurantController {

	@GetMapping("/categories/chicken")
	public String chicken() {
		return "restaurants/chicken";
	}

}

package com.home.whattoeat.controller;

import com.home.whattoeat.dto.Response;
import com.home.whattoeat.dto.menu.MenuFindResponse;
import com.home.whattoeat.dto.menu.MenuSaveRequest;
import com.home.whattoeat.dto.menu.MenuSaveResponse;
import com.home.whattoeat.dto.menu.MenuUpdateRequest;
import com.home.whattoeat.service.MenuService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurants/")
@RequiredArgsConstructor
public class MenuRestController {

	private final MenuService menuService;

	@PostMapping("/{rstId}/menus")
	public ResponseEntity<Response<MenuSaveResponse>> save(@RequestBody MenuSaveRequest request,
			@PathVariable Long rstId, Authentication authentication) {
		MenuSaveResponse result = menuService.save(request, rstId, authentication.getName());
		return ResponseEntity.created(URI.create("/api/v1/restaurants/"+rstId+"/menus/"+result.getId()))
				.body(Response.success(result));
	}

	@GetMapping("/{rstId}/menus/{menuId}")
	public ResponseEntity<Response<MenuFindResponse>> findOne(@PathVariable Long rstId,
			@PathVariable Long menuId, Authentication authentication) {
		MenuFindResponse result = menuService.findOne(rstId, menuId, authentication.getName());
		return ResponseEntity.ok().body(Response.success(result));
	}

	@GetMapping("/{rstId}/menus")
	public ResponseEntity<Response<Page<MenuFindResponse>>> findAllMyMenu(@PathVariable Long rstId,
			Pageable pageable, Authentication authentication) {
		Page<MenuFindResponse> result = menuService.findAll(rstId, pageable, authentication.getName());
		return ResponseEntity.ok().body(Response.success(result));
	}

	@PutMapping("/{rstId}/menus/{menuId}")
	public ResponseEntity<Response<Void>> update(@RequestBody MenuUpdateRequest request,
			@PathVariable Long rstId, @PathVariable Long menuId, Authentication authentication) {
		menuService.update(request, rstId, menuId, authentication.getName());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{rstId}/menus/{menuId}")
	public ResponseEntity<Response<Void>> update(@PathVariable Long rstId,
			@PathVariable Long menuId, Authentication authentication) {
		menuService.delete(rstId, menuId, authentication.getName());
		return ResponseEntity.noContent().build();
	}
}

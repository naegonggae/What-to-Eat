package com.home.whattoeat.controller;

import com.home.whattoeat.dto.restuarant.RestaurantCategoryDto;
import com.home.whattoeat.dto.restuarant.RstFindAllResponse;
import com.home.whattoeat.dto.restuarant.RstCategoryCondition;
import com.home.whattoeat.dto.restuarant.RstFindOneResponse;
import com.home.whattoeat.dto.restuarant.RstSaveRequest;
import com.home.whattoeat.dto.restuarant.RstSaveResponse;
import com.home.whattoeat.dto.restuarant.RstSearchCondition;
import com.home.whattoeat.dto.restuarant.RstUpdateRequest;
import com.home.whattoeat.service.RestaurantService;
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
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantRestController {

	private final RestaurantService restaurantService;

	@GetMapping("/{id}")
	public ResponseEntity<RstFindOneResponse> findOne(@PathVariable Long id, Authentication authentication) {
		RstFindOneResponse rstFindOneResponse = restaurantService.findOne(id,
				authentication.getName());
		return ResponseEntity.ok().body(rstFindOneResponse);
	}

	@GetMapping
	public ResponseEntity<Page<RstFindAllResponse>> findAll(Pageable pageable, Authentication authentication) {
		Page<RstFindAllResponse> findAllResponses = restaurantService.findAll(pageable, authentication.getName());
		return ResponseEntity.ok().body(findAllResponses);
	}

	@GetMapping("/categories")
	public ResponseEntity<Page<RestaurantCategoryDto>> findAllByCategory(Pageable pageable, @RequestBody RstCategoryCondition request) {
		Page<RestaurantCategoryDto> result = restaurantService.findAllByCategory(pageable, request);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/condition")
	public ResponseEntity<Page<RestaurantCategoryDto>> findAllByCondition(Pageable pageable, @RequestBody RstSearchCondition request) {
		Page<RestaurantCategoryDto> result = restaurantService.findAllByCondition(pageable, request);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping
	public ResponseEntity<RstSaveResponse> save(@RequestBody RstSaveRequest request, Authentication authentication) {
		RstSaveResponse rstSaveResponse = restaurantService.save(request, authentication.getName());
		return ResponseEntity.ok().body(rstSaveResponse);// 이거 201코드 보내야하는데
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@RequestBody RstUpdateRequest request, @PathVariable Long id, Authentication authentication) {
		restaurantService.update(id, request, authentication.getName());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
		restaurantService.delete(id, authentication.getName());
		return ResponseEntity.noContent().build();
	}

}

package com.home.whattoeat.controller;

import com.home.whattoeat.dto.restuarant.RstFindAllResponse;
import com.home.whattoeat.dto.restuarant.RstFindOneResponse;
import com.home.whattoeat.dto.restuarant.RstSaveRequest;
import com.home.whattoeat.dto.restuarant.RstSaveResponse;
import com.home.whattoeat.dto.restuarant.RstUpdateRequest;
import com.home.whattoeat.service.RestaurantService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.experimental.PackagePrivate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rsts")
@RequiredArgsConstructor
public class RstRestController {

	private final RestaurantService restaurantService;

	@GetMapping("/{id}")
	public ResponseEntity<RstFindOneResponse> findOne(@PathVariable Long id) {
		RstFindOneResponse rstFindOneResponse = restaurantService.findOne(id);
		return ResponseEntity.ok().body(rstFindOneResponse);
	}

	@GetMapping
	public ResponseEntity<Page<RstFindAllResponse>> findAll(Pageable pageable) {
		Page<RstFindAllResponse> findAllResponses = restaurantService.findAll(pageable);
		return ResponseEntity.ok().body(findAllResponses);
	}

	@PostMapping
	public ResponseEntity<RstSaveResponse> save(@RequestBody RstSaveRequest request, String username) {
		RstSaveResponse rstSaveResponse = restaurantService.save(request, username);
		return ResponseEntity.ok().body(rstSaveResponse);// 이거 201코드 보내야하는데
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@RequestBody RstUpdateRequest request, @PathVariable Long id) {
		restaurantService.update(id, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		restaurantService.delete(id);
		return ResponseEntity.noContent().build();
	}

}

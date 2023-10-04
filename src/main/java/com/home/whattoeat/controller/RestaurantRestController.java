package com.home.whattoeat.controller;

import com.home.whattoeat.config.auth.PrincipalDetails;
import com.home.whattoeat.dto.Response;
import com.home.whattoeat.dto.restuarant.RstCategoryCondition;
import com.home.whattoeat.dto.restuarant.RstFindResponse;
import com.home.whattoeat.dto.restuarant.RstSaveRequest;
import com.home.whattoeat.dto.restuarant.RstSaveResponse;
import com.home.whattoeat.dto.restuarant.RstSearchCondition;
import com.home.whattoeat.dto.restuarant.RstSearchKeyword;
import com.home.whattoeat.dto.restuarant.RstUpdateRequest;
import com.home.whattoeat.service.RestaurantService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

	// 식당 등록
	@PostMapping
	public ResponseEntity<Response<RstSaveResponse>> save(@RequestBody RstSaveRequest request,
			@AuthenticationPrincipal PrincipalDetails details) {
		RstSaveResponse result = restaurantService.save(request, details.getMember());
		return ResponseEntity.created(URI.create("/api/v1/restaurants/"+result.getId()))
				.body(Response.success(result));
	}

	// 단건 조회
	@GetMapping("/{id}")
	public ResponseEntity<Response<RstFindResponse>> findOne(@PathVariable Long id) {
		RstFindResponse result = restaurantService.findOne(id);
		return ResponseEntity.ok().body(Response.success(result));
	}

	// 내가 등록한 식당 전체 조회
	@GetMapping
	public ResponseEntity<Response<Page<RstFindResponse>>> findAllMy(Pageable pageable,
			@AuthenticationPrincipal PrincipalDetails details) {
		Page<RstFindResponse> result = restaurantService.findAllMy(pageable, details.getMember());
		return ResponseEntity.ok().body(Response.success(result));
	}

	// 카테고리별 식당 검색
	@GetMapping("/categories")
	public ResponseEntity<Response<Page<RstFindResponse>>> findAllByCategory(Pageable pageable,
			@RequestBody RstCategoryCondition request) {
		Page<RstFindResponse> result = restaurantService.findAllByCategory(pageable, request);
		return ResponseEntity.ok().body(Response.success(result));
	}

	// 식당 조건 검색
	@GetMapping("/condition")
	public ResponseEntity<Response<Page<RstFindResponse>>> findAllByCondition(Pageable pageable,
			@RequestBody RstSearchCondition request) {
		Page<RstFindResponse> result = restaurantService.findAllByCondition(pageable, request);
		return ResponseEntity.ok().body(Response.success(result));
	}

	// 식당 키워드 검색
	@GetMapping("/keyword")
	public ResponseEntity<Response<Page<RstFindResponse>>> findAllByKeyword(Pageable pageable,
			@RequestBody RstSearchKeyword request) {
		Page<RstFindResponse> result = restaurantService.findAllByKeyword(pageable, request);
		return ResponseEntity.ok().body(Response.success(result));
	}

	// 식당 수정
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@RequestBody RstUpdateRequest request,
			@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails details) {
		restaurantService.update(id, request, details.getUsername());
		return ResponseEntity.noContent().build();
	}

	// 식당 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails details) {
		restaurantService.delete(id, details.getUsername());
		return ResponseEntity.noContent().build();
	}

}

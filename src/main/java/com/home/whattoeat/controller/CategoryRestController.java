package com.home.whattoeat.controller;

import com.home.whattoeat.dto.Response;
import com.home.whattoeat.dto.category.CategoryFindResponse;
import com.home.whattoeat.dto.category.CategorySaveRequest;
import com.home.whattoeat.dto.category.CategorySaveResponse;
import com.home.whattoeat.dto.category.CategoryUpdateRequest;
import com.home.whattoeat.service.CategoryService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryRestController {

	private final CategoryService categoryService;

	@PostMapping()
	public ResponseEntity<Response<CategorySaveResponse>> save(@RequestBody CategorySaveRequest request) {
		CategorySaveResponse result = categoryService.save(request);
		return ResponseEntity.created(URI.create("/api/v1/categories/"+result.getId()))
				.body(Response.success(result));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<CategoryFindResponse>> findOne(@PathVariable Long id) {
		CategoryFindResponse result = categoryService.findOne(id);
		return ResponseEntity.ok().body(Response.success(result));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Response<Void>> findOne(@PathVariable Long id,
			@RequestBody CategoryUpdateRequest request) {
		categoryService.update(request, id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Void>> delete(@PathVariable Long id) {
		categoryService.delete(id);
		return ResponseEntity.noContent().build();
	}

}

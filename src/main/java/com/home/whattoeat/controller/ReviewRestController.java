package com.home.whattoeat.controller;

import com.home.whattoeat.config.auth.PrincipalDetails;
import com.home.whattoeat.dto.Response;
import com.home.whattoeat.dto.review.ReviewFindResponse;
import com.home.whattoeat.dto.review.ReviewSaveRequest;
import com.home.whattoeat.dto.review.ReviewSaveResponse;
import com.home.whattoeat.dto.review.ReviewUpdateRequest;
import com.home.whattoeat.service.ReviewService;
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
@RequestMapping("/api/v1/restaurants/{rstId}/reviews")
@RequiredArgsConstructor
public class ReviewRestController {

	private final ReviewService reviewService;

	@PostMapping()
	public ResponseEntity<Response<ReviewSaveResponse>> save(@RequestBody ReviewSaveRequest request,
			@PathVariable Long rstId, @AuthenticationPrincipal PrincipalDetails details) {
		ReviewSaveResponse result = reviewService.save(request, details.getMember(), rstId);
		return ResponseEntity.created(URI.create("/api/v1/restaurants/"+rstId+"/reviews/"+result.getId()))
				.body(Response.success(result));
	}

	@GetMapping()
	public ResponseEntity<Response<Page<ReviewFindResponse>>> findAll(@PathVariable Long rstId,
			Pageable pageable) {
		Page<ReviewFindResponse> result = reviewService.findAll(rstId, pageable);
		return ResponseEntity.ok().body(Response.success(result));
	}

	@PutMapping("/{reviewId}")
	public ResponseEntity<Response<Void>> update(@RequestBody ReviewUpdateRequest request,
			@PathVariable Long rstId, @PathVariable Long reviewId, @AuthenticationPrincipal PrincipalDetails details) {
		reviewService.update(request, rstId, reviewId, details.getMember());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Response<Void>> delete(@PathVariable Long rstId,
			@PathVariable Long reviewId, @AuthenticationPrincipal PrincipalDetails details) {
		reviewService.delete(rstId, reviewId, details.getMember());
		return ResponseEntity.noContent().build();
	}

}

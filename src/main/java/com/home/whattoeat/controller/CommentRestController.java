package com.home.whattoeat.controller;

import com.home.whattoeat.config.auth.PrincipalDetails;
import com.home.whattoeat.dto.Response;
import com.home.whattoeat.dto.comment.CommentFindResponse;
import com.home.whattoeat.dto.comment.CommentSaveRequest;
import com.home.whattoeat.dto.comment.CommentSaveResponse;
import com.home.whattoeat.dto.comment.CommentUpdateRequest;
import com.home.whattoeat.service.CommentService;
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
@RequestMapping("/api/v1/reviews/{reviewId}/comments")
@RequiredArgsConstructor
public class CommentRestController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<Response<CommentSaveResponse>> save(@RequestBody CommentSaveRequest request,
			@AuthenticationPrincipal PrincipalDetails details, @PathVariable Long reviewId) {
		CommentSaveResponse result = commentService.save(request, details.getMember(), reviewId);
		return ResponseEntity.created(URI.create("/api/v1/reviews/"+reviewId+"/comments/"+result.getId()))
				.body(Response.success(result));
	}

	@GetMapping
	public ResponseEntity<Response<Page<CommentFindResponse>>> findAllByReview(Pageable pageable,
			@PathVariable Long reviewId) {
		Page<CommentFindResponse> result = commentService.findAll(reviewId, pageable);
		return ResponseEntity.ok().body(Response.success(result));
	}

	@PutMapping("/{cmtId}")
	public ResponseEntity<Void> update(@RequestBody CommentUpdateRequest request,
			@PathVariable(name = "reviewId") Long reviewId, @PathVariable(name = "cmtId") Long cmtId,
			@AuthenticationPrincipal PrincipalDetails details) {
		commentService.update(request, reviewId, cmtId, details.getMember());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{cmtId}")
	public ResponseEntity<Void> delete(@AuthenticationPrincipal PrincipalDetails details,
			@PathVariable Long reviewId, @PathVariable Long cmtId) {
		commentService.delete(reviewId, cmtId, details.getMember());
		return ResponseEntity.noContent().build();
	}

}

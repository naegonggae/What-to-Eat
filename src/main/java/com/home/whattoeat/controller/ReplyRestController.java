package com.home.whattoeat.controller;

import com.home.whattoeat.config.auth.PrincipalDetails;
import com.home.whattoeat.dto.Response;
import com.home.whattoeat.dto.reply.ReplyFindResponse;
import com.home.whattoeat.dto.reply.ReplySaveRequest;
import com.home.whattoeat.dto.reply.ReplySaveResponse;
import com.home.whattoeat.dto.reply.ReplyUpdateRequest;
import com.home.whattoeat.service.ReplyService;
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
@RequestMapping("/api/v1/comments/{cmtId}/replies")
@RequiredArgsConstructor
public class ReplyRestController {

	private final ReplyService replyService;

	@PostMapping
	public ResponseEntity<Response<ReplySaveResponse>> save(@RequestBody ReplySaveRequest request,
			@AuthenticationPrincipal PrincipalDetails details, @PathVariable Long cmtId) {
		ReplySaveResponse result = replyService.save(request, details.getMember(), cmtId);
		return ResponseEntity.created(URI.create("/api/v1/comments/"+cmtId+"/replies/"+result.getId()))
				.body(Response.success(result));
	}

	@GetMapping()
	public ResponseEntity<Response<Page<ReplyFindResponse>>> findAllByComment(Pageable pageable,
			@PathVariable Long cmtId) {
		Page<ReplyFindResponse> result = replyService.findAll(cmtId, pageable);
		return ResponseEntity.ok().body(Response.success(result));
	}

	@PutMapping("/{rpId}")
	public ResponseEntity<Void> update(@RequestBody ReplyUpdateRequest request,
			@PathVariable Long cmtId, @PathVariable Long rpId,
			@AuthenticationPrincipal PrincipalDetails details) {
		replyService.update(request, cmtId, rpId, details.getMember());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{rpId}")
	public ResponseEntity<Void> delete(@AuthenticationPrincipal PrincipalDetails details,
			@PathVariable Long cmtId, @PathVariable Long rpId) {
		replyService.delete(cmtId, rpId, details.getMember());
		return ResponseEntity.noContent().build();
	}

}

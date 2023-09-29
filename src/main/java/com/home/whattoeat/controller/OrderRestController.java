package com.home.whattoeat.controller;

import com.home.whattoeat.config.auth.PrincipalDetails;
import com.home.whattoeat.dto.Response;
import com.home.whattoeat.dto.order.OrderFindResponse;
import com.home.whattoeat.dto.order.OrderSaveRequest;
import com.home.whattoeat.dto.order.OrderSaveResponse;
import com.home.whattoeat.service.OrderService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Response<OrderSaveResponse>> order(@AuthenticationPrincipal PrincipalDetails details) {
		OrderSaveResponse result = orderService.order(details.getMember());
		return ResponseEntity.created(URI.create("/api/v1/orders"+result.getId()))
				.body(Response.success(result));
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<Response<Void>> orderCancel(@PathVariable Long orderId,
			@AuthenticationPrincipal PrincipalDetails details) {
		orderService.OrderCancel(orderId, details.getMember());
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<Response<Page<OrderFindResponse>>> findAllMyOrder(Pageable pageable,
			@AuthenticationPrincipal PrincipalDetails details) {
		Page<OrderFindResponse> result = orderService.findAllMyOrder(pageable,
				details.getMember());
		return ResponseEntity.ok().body(Response.success(result));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Response<OrderFindResponse>> findOne(@PathVariable Long orderId,
			@AuthenticationPrincipal PrincipalDetails details) {
		OrderFindResponse result = orderService.findOne(orderId, details.getMember());
		return ResponseEntity.ok().body(Response.success(result));
	}
}

package com.home.whattoeat.controller;

import com.home.whattoeat.config.auth.PrincipalDetails;
import com.home.whattoeat.dto.Response;
import com.home.whattoeat.dto.cart.CartSaveRequest;
import com.home.whattoeat.dto.cart.CartSaveResponse;
import com.home.whattoeat.service.CartService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartRestController {

	private final CartService cartService;

	@PostMapping
	public ResponseEntity<Response<CartSaveResponse>> addCart(@RequestBody CartSaveRequest request,
			@AuthenticationPrincipal PrincipalDetails details) {
		CartSaveResponse result = cartService.createCart(request, details.getMember());
		return ResponseEntity.created(URI.create("/api/v1/carts/"+result.getId()))
				.body(Response.success(result));
	}

	@DeleteMapping("/{cartId}")
	public ResponseEntity<Response<Void>> addCart(@PathVariable Long cartId,
			@AuthenticationPrincipal PrincipalDetails details) {
		cartService.clearCart(cartId);
//		cartService.clearCart2(cartId);
		return ResponseEntity.noContent().build();
	}

}

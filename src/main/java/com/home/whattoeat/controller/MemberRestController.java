package com.home.whattoeat.controller;

import com.home.whattoeat.dto.member.LoginRequest;
import com.home.whattoeat.dto.member.MemberFindAllResponse;
import com.home.whattoeat.dto.member.MemberFindOneResponse;
import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberSaveResponse;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import com.home.whattoeat.dto.Response;
import com.home.whattoeat.dto.member.TokenResponse;
import com.home.whattoeat.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberRestController {

	private final MemberService memberService;

	@PostMapping("/auth/login")  //로그인
	public ResponseEntity<Response<TokenResponse>> login(
			@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
		TokenResponse tokenResponse = memberService.login(loginRequest);

		Cookie cookie = new Cookie("accessToken", tokenResponse.getAccessToken());
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.isHttpOnly();
		cookie.setMaxAge(3600); // 쿠키 유효 시간 설정 (초 단위, 여기서는 1시간)
		response.addCookie(cookie);

		//access Token 은 body 로 전송
		response.addHeader("Authorization", "Bearer " + tokenResponse.getAccessToken());

		return ResponseEntity.ok().body(Response.success(new TokenResponse(tokenResponse.getAccessToken())));
	}
	@GetMapping("/members/{id}")
	public ResponseEntity<MemberFindOneResponse> findOne(@PathVariable Long id) {
		MemberFindOneResponse findMember = memberService.findOne(id);
		return ResponseEntity.ok().body(findMember);
	}

	@GetMapping("/members")
	public ResponseEntity<Response<Page<MemberFindAllResponse>>> findAll(Pageable pageable) {
		Page<MemberFindAllResponse> memberList = memberService.findAll(pageable);
		return ResponseEntity.ok().body(Response.success(memberList));
	}

	@PostMapping("/auth/join")
	public ResponseEntity<Response<MemberSaveResponse>> save(@RequestBody MemberSaveRequest request) {
		MemberSaveResponse memberSaveResponse = memberService.saveMember(request);
		return ResponseEntity.ok().body(Response.success(memberSaveResponse));
	}

	@PutMapping("/members/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MemberUpdateRequest request) {
		memberService.update(id, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/members/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		memberService.delete(id);
		return ResponseEntity.noContent().build();
	}


}

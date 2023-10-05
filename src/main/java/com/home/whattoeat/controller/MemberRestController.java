package com.home.whattoeat.controller;

import com.home.whattoeat.dto.member.MemberLoginRequest;
import com.home.whattoeat.dto.member.MemberLoginResponse;
import com.home.whattoeat.dto.member.MemberFindResponse;
import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberSaveResponse;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import com.home.whattoeat.dto.Response;
import com.home.whattoeat.dto.member.TokenResponse;
import com.home.whattoeat.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
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

	// 회원가입
	@PostMapping("/auth/join")
	public ResponseEntity<Response<MemberSaveResponse>> save(@RequestBody MemberSaveRequest request) {
		MemberSaveResponse result = memberService.saveMember(request);
		return ResponseEntity.created(URI.create("/api/v1/auth/join/"+result.getId()))
				.body(Response.success(result));
	}

	// 로그인
	@PostMapping("/auth/login")
	public ResponseEntity<Response<MemberLoginResponse>> login(
			@RequestBody MemberLoginRequest memberLoginRequest, HttpServletResponse response) {

		TokenResponse tokenResponse = memberService.login(memberLoginRequest);

		// 쿠키 설정
		Cookie cookie = new Cookie("accessToken", tokenResponse.getAccessToken());
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.isHttpOnly();
		cookie.setMaxAge(3600); // 쿠키 유효 시간 설정 (초 단위, 여기서는 1시간)
		response.addCookie(cookie);

		// access Token 은 body 로 전송
		response.addHeader("Authorization", "Bearer " + tokenResponse.getAccessToken());

		return ResponseEntity.ok().body(Response.success(MemberLoginResponse.from()));
	}

	// 회원 단건 조회
	@GetMapping("/members/{id}")
	public ResponseEntity<Response<MemberFindResponse>> findOne(@PathVariable Long id) {
		MemberFindResponse result = memberService.findOne(id);
		return ResponseEntity.ok().body(Response.success(result));
	}

	// 회원정보 수정
	@PutMapping("/members/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MemberUpdateRequest request) {
		memberService.update(id, request);
		return ResponseEntity.noContent().build();
	}

	// 회원삭제
	@DeleteMapping("/members/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		memberService.delete(id);
		return ResponseEntity.noContent().build();
	}


}

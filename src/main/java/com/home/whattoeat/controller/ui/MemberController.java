package com.home.whattoeat.controller.ui;

import com.home.whattoeat.dto.member.MemberLoginRequest;
import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberSaveResponse;
import com.home.whattoeat.dto.member.TokenResponse;
import com.home.whattoeat.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/auth/join")
	public String joinForm(Model model) {
		model.addAttribute("memberSaveRequest", new MemberSaveRequest());
		return "members/joinForm";
	}

	@PostMapping("/auth/join")
	public String join(@Valid MemberSaveRequest request, BindingResult result) {

		if (result.hasErrors()) {
			System.out.println("BindingResult 사용됨 ");
			return "members/joinForm";
		}

		System.out.println(request.getUsername());
		MemberSaveResponse memberSaveResponse = memberService.saveMember(request);
		return "redirect:/";
	}

	@GetMapping("/auth/login")
	public String loginForm(Model model) {
		model.addAttribute("loginRequest", new MemberLoginRequest());
		return "members/loginForm";
	}

	@PostMapping("/auth/login")
	public String login(MemberLoginRequest form, BindingResult result, HttpServletResponse response) {
		System.out.println(form.getUsername());
		System.out.println(form.getPassword());
		TokenResponse tokenResponse = memberService.login(form);

		// 액세스 토큰을 쿠키에 저장
		Cookie cookie = new Cookie("accessToken", tokenResponse.getAccessToken());
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.isHttpOnly();
		cookie.setMaxAge(3600); // 쿠키 유효 시간 설정 (초 단위, 여기서는 1시간)
		response.addCookie(cookie);

//		response.addHeader("Authorization", "Bearer " + tokenResponse.getAccessToken());

		System.out.println("---");
		return "redirect:/home";
	}

	@GetMapping("/members")
	public String list() {
		return "members/memberList";
	}

}

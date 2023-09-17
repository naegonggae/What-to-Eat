package com.home.whattoeat.config.jwt;

import com.home.whattoeat.config.auth.PrincipalDetails;
import com.home.whattoeat.dto.member.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	// 여기도 리다이렉션 필요한가 쿠키생성도필요한가?
	private final JwtTokenUtil jwtTokenUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		System.out.println("successfulAuthentication 이 실행됨, 인증이 완료되었음");

		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		// 응답 헤더에 토큰을 담아서 사용자에게 리턴한다. -> 앞으로 로그인시 사용
		TokenResponse tokenResponse = jwtTokenUtil.createToken(
				principalDetails); // 문자열로 반환하도록 만들어도 괜찮을듯
		response.addHeader(jwtTokenUtil.HEADER_STRING,
				jwtTokenUtil.TOKEN_PREFIX + tokenResponse.getAccessToken());

		System.out.println(
				"successfulAuthentication - jwtToken = " + tokenResponse.getAccessToken());
		System.out.println("=====================");

	}
}
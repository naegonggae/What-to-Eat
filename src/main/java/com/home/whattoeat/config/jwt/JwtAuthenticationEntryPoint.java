package com.home.whattoeat.config.jwt;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	private final ObjectMapper objectMapper;

	public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		System.out.println("JwtAuthenticationEntryPoint 시작");

		// 여기에서 원하는 에러 응답을 생성하고 응답 코드를 설정합니다.
		String errorName = "AuthenticationException";
		String errorMessage = "알 수 없는 인증오류입니다.";

		System.out.println("errorName = " + exception.toString());
		System.out.println("errorMessage = " + errorMessage);

		if (exception.getClass().equals(InsufficientAuthenticationException.class)) {
			errorName = "InsufficientAuthenticationException";
			errorMessage = "인증이 필요한 작업입니다.";
		}
		else if (exception.getClass().equals(JWTDecodeException.class)) {
			errorName = "JWTDecodeException";
			errorMessage = "토큰 구성이 올바르지 않습니다.";
		}
		else if (exception.getClass().equals(TokenExpiredException.class)) {
			errorName = "TokenExpiredException";
			errorMessage = "토큰 기간이 만료되었습니다. 다시 로그인해주세요!";
		}

		// JSON 형식의 오류 응답을 생성합니다.
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", errorName);
		errorResponse.put("message", errorMessage);

		// HTTP 응답 설정
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// JSON 응답 데이터를 HTTP 응답에 쓰기
		objectMapper.writeValue(response.getWriter(), errorResponse);
	}
}

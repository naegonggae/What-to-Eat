package com.home.whattoeat.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
// 사용 안함
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

	// 리다이렉트가 필요한가?
	private final ObjectMapper objectMapper;

	public JwtAuthenticationFailureHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// 인증 실패 시 실행될 로직을 여기에 구현합니다.
		System.out.println("JwtAuthenticationFailureHandler - onAuthenticationFailure 시작");

		// 예를 들어, 실패 메시지를 생성합니다.
		String errorName = "AuthenticationException";
		String errorMessage = "알 수 없는 로그인 실패입니다.";

		System.out.println("exception = " + exception.toString());
		System.out.println("exception = " + exception.getClass());

		if (exception.getClass().equals(BadCredentialsException.class)) {
			errorName = "BadCredentialsException";
			errorMessage = "비밀번호가 틀렸습니다.";
		}
		else if (exception.getClass().equals(InternalAuthenticationServiceException.class)) {
			errorName = "NoSuchMemberException";
			errorMessage = "존재하지 않는 아이디입니다.";
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

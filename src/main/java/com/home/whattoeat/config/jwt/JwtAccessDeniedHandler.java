package com.home.whattoeat.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.whattoeat.dto.Response;
import com.home.whattoeat.exception.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	// 언제 쓰이는지 모르겠다.
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException e) throws IOException, ServletException {
		ObjectMapper objectMapper = new ObjectMapper();
		log.info("엑세스 권한이 없습니다.");

		JwtException exception = new JwtException("JWT 에러나서 JwtAccessDeniedHandler 에 들어왔습니다.");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, 401,
				exception.getMessage());
		Response<ErrorResponse> error = Response.error(errorResponse);

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.getWriter()
				.write(objectMapper.writeValueAsString(error)); //Response 객체를 response 의 바디값으로 파싱

//		log.error("[handle] 접근이 거부되어 경로 리다이렉트");
//		response.sendRedirect("/index");


	}
}

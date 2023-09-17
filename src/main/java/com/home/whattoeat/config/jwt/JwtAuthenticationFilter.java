package com.home.whattoeat.config.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.whattoeat.config.auth.PrincipalDetails;
import com.home.whattoeat.dto.member.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager; // Authentication 객체 만들어서 리턴

	@Autowired
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		setFilterProcessesUrl("/api/v1/auth/login");
	}

	// 로그인 요청을 했을 때 인증역할
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {

		System.out.println("JwtAuthenticationFilter : 로그인 시도중");

		// 1. 유저의 request 에 있는 username 과 password 를 파싱해서 자바 Object 로 받기
		ObjectMapper om = new ObjectMapper();
		LoginRequest loginRequestDto = null;
		try {
			loginRequestDto = om.readValue(request.getInputStream(), LoginRequest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("JwtAuthenticationFilter : " + loginRequestDto.toString());

		// 2. username, password 토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),
						loginRequestDto.getPassword());

		System.out.println("JwtAuthenticationFilter : 토큰생성완료");

		// 3. authenticationManager 에 토큰을 넣어서 던지면 인증을 해줌 -> 인증을 하면 authentication 받음
		// authenticationManager 로 로그인시도를 하면 PrincipalDetailsService 가 호출이 됨 ->  loadUserByUsername 호출
		System.out.println("JwtAuthenticationFilter : authenticationManager 에게 토큰 넘김");
		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		// 4. 내가 입력한 username 과 password 가 SecurityContext 에서 꺼내지면 로그인이 정상적으로 되었다는 뜻
		// 왜? SecurityContext 에 저장되려면 authenticationManager 가 인증을 끝낸것을 의미
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		System.out.println("JwtAuthenticationFilter : 로그인 완료됨");
		System.out.println("JwtAuthenticationFilter - principalDetails = " + principalDetails.getUsername());
		System.out.println("=====================");

		return authentication;
	}
}

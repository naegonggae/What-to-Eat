package com.home.whattoeat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.whattoeat.config.jwt.JwtAccessDeniedHandler;
import com.home.whattoeat.config.jwt.JwtAuthenticationEntryPoint;
import com.home.whattoeat.config.jwt.JwtAuthenticationFailureHandler;
import com.home.whattoeat.config.jwt.JwtAuthenticationFilter;
import com.home.whattoeat.config.jwt.JwtAuthenticationSuccessHandler;
import com.home.whattoeat.config.jwt.JwtAuthorizationFilter;
import com.home.whattoeat.config.jwt.JwtExceptionFilter;
import com.home.whattoeat.config.jwt.JwtTokenUtil;
import com.home.whattoeat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig {

	private final MemberRepository memberRepository;
	private final ObjectMapper objectMapper;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAuthenticationSuccessHandler successHandler;
	private final JwtAuthenticationFailureHandler failureHandler;
	private final JwtTokenUtil jwtTokenUtil;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		System.out.println("필터 시작");

			http
					.authorizeHttpRequests(authorize -> authorize
					// org.h2.server.web.JakartaWebServlet: H2 데이터베이스의 웹 콘솔용 서블릿
					// org.springframework.web.servlet.DispatcherServlet: Spring MVC 웹 애플리케이션의 디스패처 서블릿
					// 위의 두개의 서블릿이 동일한 URL 패턴에 매핑되어 있어 Spring MVC 서블릿을 사용하도록 설정
							.requestMatchers(new MvcRequestMatcher(introspector, "/**")).permitAll()
							.requestMatchers(new MvcRequestMatcher(introspector, "/api/v1/auth/**")).permitAll()
							// api 만 인증이 필요한 요청으로 사용
							.requestMatchers(new MvcRequestMatcher(introspector, "/api/v1/**")).authenticated())
					.exceptionHandling((exceptionHandling) ->
							exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
					.csrf((csrf) -> csrf.disable())
					.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않겠다.
					.cors().and()
					.exceptionHandling().accessDeniedHandler(new JwtAccessDeniedHandler())
					.and()
					.addFilterBefore(new JwtExceptionFilter(objectMapper), JwtAuthorizationFilter.class)
					.apply(new MyCustomDsl()); // 커스텀 필터 등록 // authenticationManager 파라미터를 던져줘야함

		return http.build();

	}

	public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
			JwtAuthenticationFilter att = new JwtAuthenticationFilter(authenticationManager);
			att.setAuthenticationSuccessHandler(successHandler);
			att.setAuthenticationFailureHandler(failureHandler);

			JwtAuthorizationFilter atr = new JwtAuthorizationFilter(authenticationManager, memberRepository, jwtTokenUtil);
			http
					//.addFilterBefore(att, UsernamePasswordAuthenticationFilter.class)
					.addFilterBefore(atr, BasicAuthenticationFilter.class);
		}
	}
}

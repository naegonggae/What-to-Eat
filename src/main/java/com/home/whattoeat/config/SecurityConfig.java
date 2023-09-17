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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		System.out.println("필터 시작");

			http

					.authorizeHttpRequests(authorize -> authorize
							.requestMatchers("/api/v1/auth/**").permitAll()
							.anyRequest().authenticated())
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
					.addFilterBefore(att, UsernamePasswordAuthenticationFilter.class)
					.addFilterBefore(atr, BasicAuthenticationFilter.class);
		}
	}
}

package com.home.whattoeat.config.auth;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import com.home.whattoeat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService - loadUserByUsername 실행");

		Member memberEntity = memberRepository.findByUsername(username)
				.orElseThrow(NoSuchMemberException::new);
		System.out.println("PrincipalDetailsService - memberEntity = " + memberEntity.getUsername());
		System.out.println("PrincipalDetailsService - loadUserByUsername 종료");

		return new PrincipalDetails(memberEntity);
	}
}

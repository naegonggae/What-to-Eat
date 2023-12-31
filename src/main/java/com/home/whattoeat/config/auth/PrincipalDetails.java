package com.home.whattoeat.config.auth;

import com.home.whattoeat.domain.Member;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
@Getter
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

	private final Member member;


//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		Collection<GrantedAuthority> authorities = new ArrayList<>();
//		member.getRoleList().forEach(r -> {
//			authorities.add(() -> {
//				return r;
//			});
//		});
//		return authorities;
//	}
	public String getEmail() { return member.getEmail(); }


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}

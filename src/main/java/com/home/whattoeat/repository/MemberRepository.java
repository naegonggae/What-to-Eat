package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByUsername(String username);
	boolean existsByEmail(String email);

	Optional<Member> findByUsername(String username);

}

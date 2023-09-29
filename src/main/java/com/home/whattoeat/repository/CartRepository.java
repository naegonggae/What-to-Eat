package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Cart;
import com.home.whattoeat.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

	boolean existsByMember(Member member);
	Optional<Cart> findByMember(Member member);

}

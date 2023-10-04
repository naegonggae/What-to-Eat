package com.home.whattoeat.repository.order;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

	Page<Order> findAllByMember(Member member, Pageable pageable);

}

package com.home.whattoeat.repository;

import com.home.whattoeat.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

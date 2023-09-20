package com.home.whattoeat.repository;

import com.home.whattoeat.domain.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderMenu, Long> {

}

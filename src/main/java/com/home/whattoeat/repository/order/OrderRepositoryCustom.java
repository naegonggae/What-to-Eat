package com.home.whattoeat.repository.order;

import com.home.whattoeat.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

	Page<Order> findAllOrder(String username, Pageable pageable);

}

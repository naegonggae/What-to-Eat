package com.home.whattoeat.repository;

import com.home.whattoeat.domain.CartMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartMenuRepository extends JpaRepository<CartMenu, Long> {

}

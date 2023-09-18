package com.home.whattoeat.domain;

import com.home.whattoeat.dto.menu.MenuUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Builder
@Table(name = "menus")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "menus_id")
	private Long id;
	private String name;
	private String description;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	private int price;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	// 재료
	// 칼로리
	// 알레르기 정보
	// 옵션
	// 인기도
	// 사진

	public void update(MenuUpdateRequest request) {
		this.name = request.getName();
		this.description = request.getDescription();
		this.price = request.getPrice();
	}
}

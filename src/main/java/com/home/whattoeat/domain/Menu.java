package com.home.whattoeat.domain;

import static lombok.AccessLevel.PROTECTED;

import com.home.whattoeat.dto.menu.MenuSaveRequest;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "menus")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Menu extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "menus_id")
	private Long id;
	private String name;
	private String description;
	private Integer price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	// 연관관계 메서드 // 이거 사용해서 저장해줘야 식당 삭제할때 메뉴들이 삭제될거야 아마
	public void addRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
		restaurant.getMenuList().add(this);
	}

	// 생성 메서드 //
	public static Menu createMenu(MenuSaveRequest request, Restaurant restaurant) {
		return new Menu(request, restaurant);
	}
	public Menu(MenuSaveRequest request, Restaurant restaurant) {
		this.name = request.getName();
		this.description = request.getDescription();
		this.price = request.getPrice();
		this.addRestaurant(restaurant);
	}

	// 비즈니스 메서드 //
	public void update(MenuUpdateRequest request) {
		this.name = request.getName();
		this.description = request.getDescription();
		this.price = request.getPrice();
	}

	public void removeRestaurant() {
		this.restaurant = null;
	}

	@Override
	public String toString() {
		return "Menu{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", price=" + price +
				'}';
	}

}

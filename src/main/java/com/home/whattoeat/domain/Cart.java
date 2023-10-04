package com.home.whattoeat.domain;

import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class Cart extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "cart_id")
	private Long id;

	@JsonIgnore
	@OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE)
	private List<CartMenu> cartMenus = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	// 생성 메서드 //
	public static Cart createCart(Member member) {
		return new Cart(member);
	}
	public Cart(Member member) {
		this.member = member;
	}

}

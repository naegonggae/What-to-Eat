package com.home.whattoeat.domain;

import com.home.whattoeat.dto.restuarant.RstUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseEntity {

	@Id @GeneratedValue
	@Column(name = "restaurant_id")
	private Long id;
	private String name;
	private String phoneNumber;
	private String cuisineType; // enum

	private Address address;
	// 영업시간
	// 가게 소개
	// 평균가격
	// 사진
	// 예약 가능여부
	// 위도경도
	// 평점
	// 운영자 정보
	// 온라인 예약 링크, 인스타 링크
	// 결제 수단
	// 예약 현황
	// 운영 중단 일정
	// 프로모션 정보

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "restaurant")
	private List<Menu> menuList = new ArrayList<>();

//	@OneToMany(mappedBy = "restaurant")
//	private List<RestaurantCategory> categoryList = new ArrayList<>();

	public void update(RstUpdateRequest request) {
		this.name = request.getName();
		this.phoneNumber = request.getPhoneNumber();
		this.cuisineType = request.getCuisineType();
	}

}

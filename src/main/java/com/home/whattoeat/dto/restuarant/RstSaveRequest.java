package com.home.whattoeat.dto.restuarant;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RstSaveRequest {

	// data init X
	private String name;
	private String phoneNumber;
	private String description;
	private String city;
	private String street;
	private String zipcode;
	private Integer minOrderAmount;
	private Integer maxOrderAmount;
	private List<String> categoryNames; // 여러 카테고리를 입력받을 수 있음

	// data init O
//	private String name;
//	private String phoneNumber;
//	private String description;
//	private String city;
//	private String street;
//	private String zipcode;
//	private Double starRating;
//	private Long numberOfOrders;
//	private Integer minOrderAmount;
//	private Integer maxOrderAmount;
//	private Integer reviewCount;
//	private List<String> categoryNames; // 여러 카테고리를 입력받을 수 있음

}

package com.home.whattoeat.dto.restuarant;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RstUpdateRequest {

	private String name;
	private String phoneNumber;
	private double starRating; // 리뷰할때 구현
	private Long numberOfOrders; // 리뷰할때 구현 지금은 하드 코딩해버리자
	private int minOrderAmount;
	private int maxOrderAmount;	private List<String> categoryName; // 여러 카테고리를 입력받을 수 있음
}

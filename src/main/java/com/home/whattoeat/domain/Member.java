package com.home.whattoeat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString //주문이나 주문상세까지 파고드니까 toString 하고 싶으면 조인한건 빼고 가야지
public class Member extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	private String username;
	private String email;
	private String password;
	private String cardNumber;
	private String phoneNumber;
	private String role; // enum
	@Embedded
	private Address address;

	@JsonIgnore
	@OneToMany(mappedBy = "member") // 주인이 아님을 표시 Order 에 있는 member 와 연결이 된다는 표시
	private List<Order> orderList = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<Restaurant> restaurantList = new ArrayList<>();

	public Member(MemberSaveRequest request) {
		this.username = request.getUsername();
		this.email = request.getEmail();
		this.password = request.getPassword();
	}

	public void update(MemberUpdateRequest request) {
		this.username = request.getUsername();
		this.email = request.getEmail();
	}


	@Override
	public String toString() {
		return "Member{" +
				"id=" + id +
				", username='" + username + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", cardNumber='" + cardNumber + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", role='" + role + '\'' +
				", address=" + address +
				'}';
	}
}

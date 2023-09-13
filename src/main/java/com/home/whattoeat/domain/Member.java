package com.home.whattoeat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.ToString;
import org.hibernate.annotations.Where;

@Entity @Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
	@OneToMany(mappedBy = "member")
	private List<Order> orderList = new ArrayList<>();

	public Member(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public void update(MemberUpdateRequest request) {
		this.username = request.getUsername();
		this.email = request.getEmail();
	}


}

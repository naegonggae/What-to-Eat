package com.home.whattoeat.domain;

import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Where;

@Entity
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted_at is NULL")
@ToString
public class Member extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;
	@Column(unique = true, nullable = false, length = 20)
	private String username;
	@Column(unique = true, nullable = false, length = 20)
	private String email;
	@Column(nullable = false, length = 128)
	private String password;
	@Column(nullable = false, length = 15)
	private String phoneNumber;
	@Enumerated(EnumType.STRING)
	private MemberRole role; // ADMIN, USER

	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE) // 회원을 삭제하면 회원의 주문엔티티 삭제
	private List<Order> orderList = new ArrayList<>();

	// 생성 메서드 //
	public static Member createMember(MemberSaveRequest request, String encodedPassword) {
		return new Member(request, encodedPassword);
	}
	public Member(MemberSaveRequest request, String encodedPassword) {
		this.username = request.getUsername();
		this.email = request.getEmail();
		this.password = encodedPassword;
		this.phoneNumber = request.getPhoneNumber();
		this.role = MemberRole.USER;
	}

	// 비즈니스 메서드 //
	public void update(MemberUpdateRequest request) {
		this.username = request.getUsername();
		this.email = request.getEmail();
		this.phoneNumber = request.getPhoneNumber();
	}

}

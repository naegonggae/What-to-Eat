package com.home.whattoeat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
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
	@OneToMany(mappedBy = "user")
	private List<Order> orderList = new ArrayList<>();

}

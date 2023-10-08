package com.home.whattoeat.service;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.MemberRole;
import com.home.whattoeat.repository.CommentRepository;
import com.home.whattoeat.repository.MemberRepository;
import com.home.whattoeat.repository.ReplyRepository;
import com.home.whattoeat.repository.ReviewRepository;
import com.home.whattoeat.repository.restaurant.RestaurantRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

	@Mock
	MemberRepository memberRepository;
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Mock
	RestaurantRepository restaurantRepository;
	@Mock
	ReviewRepository reviewRepository;
	@Mock
	CommentRepository commentRepository;
	@Mock
	ReplyRepository replyRepository;

	public Member member = new Member(
			1L, "홍길동", "naver@naver.com", "1234",
			"010-1234-1234", MemberRole.USER, null);

}

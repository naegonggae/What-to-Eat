package com.home.whattoeat.service;

import com.home.whattoeat.domain.Address;
import com.home.whattoeat.domain.Category;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.MemberRole;
import com.home.whattoeat.domain.Menu;
import com.home.whattoeat.domain.Order;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.domain.RestaurantCategory;
import com.home.whattoeat.domain.RestaurantStatus;
import com.home.whattoeat.domain.Review;
import com.home.whattoeat.repository.CategoryRepository;
import com.home.whattoeat.repository.CommentRepository;
import com.home.whattoeat.repository.MemberRepository;
import com.home.whattoeat.repository.MenuRepository;
import com.home.whattoeat.repository.ReplyRepository;
import com.home.whattoeat.repository.RestaurantCategoryRepository;
import com.home.whattoeat.repository.ReviewRepository;
import com.home.whattoeat.repository.order.OrderRepository;
import com.home.whattoeat.repository.restaurant.RestaurantRepository;
import java.util.ArrayList;
import java.util.List;
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
	CategoryRepository categoryRepository;
	@Mock
	RestaurantRepository restaurantRepository;
	@Mock
	RestaurantCategoryRepository rcRepository;
	@Mock
	MenuRepository menuRepository;
	@Mock
	OrderRepository orderRepository;
	@Mock
	ReviewRepository reviewRepository;
	@Mock
	CommentRepository commentRepository;
	@Mock
	ReplyRepository replyRepository;

	// Member //
	List<Order> orderList = new ArrayList<>();
	public Member member = new Member(
			1L, "홍길동", "naver@naver.com", "1234",
			"010-1234-1234", MemberRole.USER, orderList);
	List<Order> orderList2 = new ArrayList<>();
	public Member member2 = new Member(
			2L, "유재석", "google@naver.com", "1234",
			"010-9999-1234", MemberRole.USER, orderList2);
	// Category //
	public Category category = new Category(1L, "햄버거");
	public Category category2 = new Category(2L, "패스트푸드");
	public Category category3 = new Category(3L, "식사용");
	public Category category4 = new Category(1L, "피자");
	public RestaurantCategory rc = new RestaurantCategory(category);
	public RestaurantCategory rc2 = new RestaurantCategory(category2);
	public RestaurantCategory rc3 = new RestaurantCategory(category3);
	// Restaurant //
	List<Menu> menuList = new ArrayList<>();
	List<Review> reviewList = new ArrayList<>();
	List<RestaurantCategory> rcl = new ArrayList<>() {{add(rc); add(rc2); add(rc3);}};
	public Restaurant restaurant = new Restaurant(1L, "맥도날드", "010-1234-1234", "맥도날드 입니다.",
			new Address("안양시", "삼덕로", "123-123"), 0.0, 0L,
			1000, 10000, 0, RestaurantStatus.OPEN, member,
			menuList, reviewList, rcl);
	List<Menu> menuList2 = new ArrayList<>();
	List<Review> reviewList2 = new ArrayList<>();
	List<RestaurantCategory> rcl2 = new ArrayList<>() {{add(rc); add(rc2); add(rc3);}};
	public Restaurant restaurant2 = new Restaurant(2L, "롯데리아", "010-1234-1234", "롯데리아 입니다.",
			new Address("안양시", "삼덕로", "123-123"), 0.0, 0L,
			1000, 10000, 0, RestaurantStatus.OPEN, member,
			menuList2, reviewList2, rcl2);
	// Menu //
	public Menu menu = new Menu(1L, "빅맥", "빅맥입니다.", 6000, restaurant);
	public Menu menu2 = new Menu(2L, "슈슈버거", "슈슈버거입니다.", 6500, restaurant);

}

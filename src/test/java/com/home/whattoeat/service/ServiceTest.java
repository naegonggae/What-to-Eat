package com.home.whattoeat.service;

import com.home.whattoeat.domain.Address;
import com.home.whattoeat.domain.Cart;
import com.home.whattoeat.domain.CartMenu;
import com.home.whattoeat.domain.Category;
import com.home.whattoeat.domain.Comment;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.MemberRole;
import com.home.whattoeat.domain.Menu;
import com.home.whattoeat.domain.Order;
import com.home.whattoeat.domain.OrderMenu;
import com.home.whattoeat.domain.OrderStatus;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.domain.RestaurantCategory;
import com.home.whattoeat.domain.RestaurantStatus;
import com.home.whattoeat.domain.Review;
import com.home.whattoeat.repository.CartMenuRepository;
import com.home.whattoeat.repository.CartRepository;
import com.home.whattoeat.repository.CategoryRepository;
import com.home.whattoeat.repository.CommentRepository;
import com.home.whattoeat.repository.MemberRepository;
import com.home.whattoeat.repository.MenuRepository;
import com.home.whattoeat.repository.ReplyRepository;
import com.home.whattoeat.repository.RestaurantCategoryRepository;
import com.home.whattoeat.repository.ReviewRepository;
import com.home.whattoeat.repository.order.OrderRepository;
import com.home.whattoeat.repository.restaurant.RestaurantRepository;
import java.time.LocalDateTime;
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
	CartRepository cartRepository;
	@Mock
	CartMenuRepository cartMenuRepository;
	@Mock
	OrderRepository orderRepository;
	@Mock
	ReviewRepository reviewRepository;
	@Mock
	CommentRepository commentRepository;
	@Mock
	ReplyRepository replyRepository;
	@Mock
	CartMenuService cartMenuService;

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
	// Cart //
	List<CartMenu> cartMenuList = new ArrayList<>();
	public Cart cart = new Cart(1L, cartMenuList, member);
	public CartMenu cartMenu = new CartMenu(cart, menu, 10, 6000);
	public CartMenu cartMenu2 = new CartMenu(1L, 10, 6000, cart, menu);
	List<CartMenu> cartMenuList2 = new ArrayList<>(){{ add(cartMenu2);}};
	public Cart cart2 = new Cart(1L, cartMenuList2, member);
	// Order //
	List<OrderMenu> orderMenuList = new ArrayList<>();
	public Order order = new Order(
			1L, "케챱 많이 주세요.", 60000,
			LocalDateTime.of(2023,10, 9, 19, 8), OrderStatus.ORDER,
			member, restaurant, orderMenuList, null);
	List<OrderMenu> orderMenuList2 = new ArrayList<>();
	public Order order2 = new Order(
			2L, "배고프니 빨리 배달해주세요.", 60000,
			LocalDateTime.of(2023,10, 9, 19, 8), OrderStatus.ORDER,
			member2, restaurant2, orderMenuList2, null);
	// Review //
	List<Comment> commentList = new ArrayList<>();
	public Review review = new Review(
			1L, "정말 맛있어요.", 4.5, restaurant, commentList, member);
	List<Comment> commentList2 = new ArrayList<>();
	public Review review2 = new Review(
			2L, "정말 맛없어요.", 1.0, restaurant, commentList2, member2);

}

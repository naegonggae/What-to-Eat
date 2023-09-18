package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.domain.Restaurant;
import com.home.whattoeat.dto.restuarant.RstFindAllResponse;
import com.home.whattoeat.dto.restuarant.RstFindOneResponse;
import com.home.whattoeat.dto.restuarant.RstSaveRequest;
import com.home.whattoeat.dto.restuarant.RstSaveResponse;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import com.home.whattoeat.exception.reataurant.NoSuchRestaurantException;
import com.home.whattoeat.repository.MemberRepository;
import com.home.whattoeat.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

	@Mock
	RestaurantRepository restaurantRepository;

	@Mock
	MemberRepository memberRepository;

	@InjectMocks
	private RestaurantService restaurantService;

//	@BeforeEach
//	public void setUp() {
//		//한번에 묶을까
//	}

	@Test
	@DisplayName("사용자가 존재하고 유효한 요청으로 음식점을 저장할 때, 레스토랑 저장 성공")
	public void save() {
	    String username = "홍길동";
		RstSaveRequest request = new RstSaveRequest("배떡", "01012341234", "분식");

		Member member = Member.builder().username(username).build();
		Restaurant restaurant = Restaurant.builder()
				.id(1L)
				.name(request.getName())
				.phoneNumber(request.getPhoneNumber())
				.cuisineType(request.getCuisineType())
				.build();

		when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
		when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

		RstSaveResponse result = restaurantService.save(request, username);

		assertThat(1L).isEqualTo(result.getId());
		verify(memberRepository, times(1)).findByUsername(username);
		verify(restaurantRepository, times(1)).save(any(Restaurant.class));
	}
	
	@Test
	@DisplayName("사용자가 존재하지 않을 때, 레스토랑 저장 실패")
	public void saveFail1() {

		String username = "홍길동";
		RstSaveRequest request = new RstSaveRequest("배떡", "01012341234", "분식");

		when(memberRepository.findByUsername(username)).thenThrow(new NoSuchMemberException());

		assertThatThrownBy(() -> restaurantService.save(request, username))
				.isInstanceOf(NoSuchMemberException.class)
				.hasMessageContaining("존재하지 않는 회원입니다.");

		verify(memberRepository, times(1)).findByUsername(username);
	}

	@Test
	@DisplayName("조회한 레스토랑이 존재할 때, 레스토랑 단건 조회 성공")
	public void findOne() {
		Member savedMember = Member.builder()
				.id(1L)
				.username("홍길동")
				.email("email@naver.com")
				.password("1234")
				.build();
		Long id = 1L;
		Restaurant restaurant = Restaurant.builder()
				.id(id)
				.name("배민")
				.phoneNumber("01012341234")
				.cuisineType("분식")
				.member(savedMember)
				.build();

	    when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
		RstFindOneResponse result = restaurantService.findOne(id, savedMember.getUsername());

		assertThat("배민").isEqualTo(result.getName());
		assertThat("01012341234").isEqualTo(result.getPhoneNumber());
		assertThat("분식").isEqualTo(result.getCuisineType());

		verify(restaurantRepository, times(1)).findById(id);
	}

	@Test
	@DisplayName("해당 레스토랑을 조회할 수 없을 때, 레스토랑 단건 조회 실패")
	public void findOneFail1() {

		String username = "상훈";

		Long id = 1L;

		when(restaurantRepository.findById(id)).thenThrow(new NoSuchRestaurantException());

		assertThatThrownBy(() -> restaurantService.findOne(id, username))
				.isInstanceOf(NoSuchRestaurantException.class)
				.hasMessageContaining("존재하지 않는 음식점입니다.");
		verify(restaurantRepository, times(1)).findById(id);
	}

	@Test
	@DisplayName("전체 레스토랑 조회 성공")
	public void findAll() {
		Member savedMember = Member.builder()
				.id(1L)
				.username("홍길동")
				.email("email@naver.com")
				.password("1234")
				.build();

		Restaurant restaurant1 = Restaurant.builder()
				.id(1L).name("배민").phoneNumber("01012341234").cuisineType("분식").member(savedMember).build();
		Restaurant restaurant2 = Restaurant.builder()
				.id(2L).name("신전").phoneNumber("01099999999").cuisineType("분식").member(savedMember).build();

		Pageable pageable = PageRequest.of(0, 20);
		Page<Restaurant> page = new PageImpl<>(List.of(restaurant1, restaurant2));

		when(memberRepository.findByUsername(savedMember.getUsername())).thenReturn(Optional.of(savedMember));
		when(restaurantRepository.findAllByMember(savedMember, pageable)).thenReturn(page);
		Page<RstFindAllResponse> result = restaurantService.findAll(pageable, savedMember.getUsername());

		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result.getContent().get(0).getName()).isEqualTo("배민");
		assertThat(result.getContent().get(1).getName()).isEqualTo("신전");

		verify(memberRepository, times(1)).findByUsername(savedMember.getUsername());
		verify(restaurantRepository, times(1)).findAllByMember(savedMember, pageable);
	}

	@Test
	@DisplayName("회원의 id가 존재 할 때, 레스토랑 수정 성공")
	public void update() {

		Long id = 1L;


	}

}
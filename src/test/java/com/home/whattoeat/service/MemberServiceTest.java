package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberSaveResponse;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.exception.member.DuplicateEmailException;
import com.home.whattoeat.exception.member.DuplicateUsernameException;
import com.home.whattoeat.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	MemberRepository memberRepository;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@InjectMocks // mock 들을 의존성 주입해줌
	private MemberService memberService;


	//member service 에서 테스트해야할것은 회원인 잘 저장됐는지가 아니다.
	@Test
	@DisplayName("회원 저장 성공")
	public void saveMember() throws Exception {

		// 실제 테스트할 메서드에 들어갈 파라미터
		MemberSaveRequest memberSaveRequest =
				new MemberSaveRequest("홍길동", "email@naver.com", "1234");


		// 환경설정
		Member savedMember = Member.builder()
				.id(1L)
				.username(memberSaveRequest.getUsername())
				.email(memberSaveRequest.getEmail())
				.password(bCryptPasswordEncoder.encode(memberSaveRequest.getPassword()))
				.build();

		when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

		// 환경설정한대로면 이렇게 나와야한다.
		MemberSaveResponse memberSaveResponse = MemberSaveResponse.form(savedMember);


		// 서비스 메서드로 만든거랑 mock 하면서 만든거랑 같다
		// 실제 메서드를 실행하기까지 위의 환경들이 다 적용받는다. -> 환경설정한거보다 테스트할 메서드의 실행이 위에 있다면 null 이 뜰수도있다.
		assertThat(memberService.saveMember(memberSaveRequest))
				.usingRecursiveComparison().isEqualTo(memberSaveResponse);
	}

	@Test
	@DisplayName("회원저장 실패 - 중복된 이름")
	public void saveFail1() throws Exception {
		MemberSaveRequest memberSaveRequest =
				new MemberSaveRequest("홍길동", "email@naver.com", "1234");

		given(memberRepository.existsByUsername("홍길동")).willReturn(true);

		assertThatThrownBy(() -> memberService.saveMember(memberSaveRequest))
				.isInstanceOf(DuplicateUsernameException.class);
	}

	@Test
	@DisplayName("회원저장 실패 - 중복된 이메일")
	public void saveFail2() throws Exception {
		// 실패 테스트는 환경을 설정하고 테스트할 메서드를 실행하면 특정에러가 나와야한다.
		// 테스트할 메서드에 들어갈 파라미터
		MemberSaveRequest memberSaveRequest =
				new MemberSaveRequest("홍길동", "email@naver.com", "1234");

		// 테스트 환경 설정
		given(memberRepository.existsByEmail("email@naver.com")).willReturn(true);

		// 테스트할 메서드 실행
		// 환경설정한 결과 도출한 것과 비교
		assertThatThrownBy(() -> memberService.saveMember(memberSaveRequest))
				.isInstanceOf(DuplicateEmailException.class);
	}

	@Test
	@DisplayName("회원 단건 조회 성공")
	public void findOne() throws Exception {

		Member savedMember = Member.builder()
				.id(1L)
				.username("홍길동")
				.email("email@naver.com")
				.password("1234")
				.build();

		given(memberRepository.findById(1L)).willReturn(Optional.of(savedMember));

		assertThat(memberService.findOne(1L).getId()).isEqualTo(savedMember.getId());
		assertThat(memberService.findOne(1L).getUsername()).isEqualTo(savedMember.getUsername());
		assertThat(memberService.findOne(1L).getEmail()).isEqualTo(savedMember.getEmail());
		assertThat(memberService.findOne(1L).getPassword()).isEqualTo(savedMember.getPassword());
	}

	@Test
	@DisplayName("회원 수정 성공")
	public void update() throws Exception {
		// 테스트할 메서드에 들어갈 파라미터
		MemberUpdateRequest memberUpdateRequest =
				new MemberUpdateRequest("강호동", "email@google.com");
		// 환경설정
		Member savedMember = Member.builder()
				.id(1L)
				.username("홍길동")
				.email("email@naver.com")
				.password("1234")
				.build();
		given(memberRepository.findById(1L)).willReturn(Optional.of(savedMember));

		// 테스트할 메서드 실행
		memberService.update(1L, memberUpdateRequest);

		// given 으로 저장한거 꺼낼수는 있구나, 결과 확인
		assertThat(memberRepository.findById(1L).orElseThrow().getUsername()).isEqualTo("강호동");

	}

	@Test
	@DisplayName("회원 수정시 중복되는 이름을 선택")
	public void updateFail1() throws Exception {
		// 테스트할 메서드에 들어갈 파라미터
		MemberUpdateRequest memberUpdateRequest =
				new MemberUpdateRequest("이상훈", "email@google.com");
		// 환경설정
		Member savedMember = Member.builder()
				.id(1L)
				.username("홍길동")
				.email("email@naver.com")
				.password("1234")
				.build();

		given(memberRepository.findById(1L)).willReturn(Optional.of(savedMember));
//		given(memberRepository.findById(2L)).willReturn(Optional.of(savedMember2));

		// 테스트할 메서드 실행
//		memberService.update(1L, memberUpdateRequest);

		given(memberRepository.existsByUsername("이상훈")).willReturn(true);
		// given 으로 저장한거 꺼낼수는 있구나, 결과 확인
//		assertThat(memberRepository.findById(1L).orElseThrow().getUsername()).isEqualTo("이상훈");
		assertThatThrownBy(() -> memberService.update(1L, memberUpdateRequest))
				.isInstanceOf(DuplicateUsernameException.class);

	}

	@Test
	@DisplayName("회원 수정시 중복되는 이메일을 선택")
	public void updateFail2() throws Exception {
		// 테스트할 메서드에 들어갈 파라미터
		MemberUpdateRequest memberUpdateRequest =
				new MemberUpdateRequest("홍길동", "hi@google.com");
		// 환경설정
		Member savedMember = Member.builder()
				.id(1L)
				.username("홍길동")
				.email("email@naver.com")
				.password("1234")
				.build();

		given(memberRepository.findById(1L)).willReturn(Optional.of(savedMember));

		given(memberRepository.existsByEmail("hi@google.com")).willReturn(true);

		// given 으로 저장한거 꺼낼수는 있구나, 결과 확인
		assertThatThrownBy(() -> memberService.update(1L, memberUpdateRequest))
				.isInstanceOf(DuplicateEmailException.class);

	}

	@Test
	@DisplayName("회원 삭제 성공")
	public void delete() throws Exception {
		Member savedMember = Member.builder()
				.id(1L)
				.username("홍길동")
				.email("email@naver.com")
				.password("1234")
				.build();
		given(memberRepository.findById(1L)).willReturn(Optional.of(savedMember));

		// 에러 발생하지 않으면 삭제 됨
		assertDoesNotThrow(() -> memberService.delete(savedMember.getId()));
	}

}
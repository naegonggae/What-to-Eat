package com.home.whattoeat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.home.whattoeat.domain.MemberRole;
import com.home.whattoeat.dto.member.MemberFindResponse;
import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberSaveResponse;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.exception.member.ConstraintViolationMemberException;
import com.home.whattoeat.exception.member.DuplicateEmailException;
import com.home.whattoeat.exception.member.DuplicateUsernameException;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class MemberServiceTest extends ServiceTest {

	@InjectMocks // mock 들을 의존성 주입해줌
	private MemberService memberService;

	@BeforeEach
	void setUp() {
		member = new Member(
				1L, "홍길동", "naver@naver.com", "1234",
				"010-1234-1234", MemberRole.USER, null);
	}

	@Nested
	@DisplayName("saveMember 메서드는")
	class SaveMemberCase {

		// given
		MemberSaveRequest request = new MemberSaveRequest(
				"홍길동", "nave@naver.com","1234","010-1234-1234");
		@Test
		@DisplayName("이메일과 아이디가 중복되지 않으면 member 를 생성")
		public void success_saveMember() {

			// when
			when(memberRepository.existsByEmail(request.getEmail())).thenReturn(false);
			when(memberRepository.existsByUsername(request.getUsername())).thenReturn(false);
			when(bCryptPasswordEncoder.encode(request.getPassword())).thenReturn("암호화된 비밀번호");
			Member member = new Member(1L, request, "암호화된 비밀번호");
			when(memberRepository.save(any(Member.class))).thenReturn(member);

			// then
			MemberSaveResponse result = memberService.saveMember(request);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getUsername()).isEqualTo("홍길동");
			assertThat(result.getEmail()).isEqualTo("nave@naver.com");
			assertThat(result.getPhoneNumber()).isEqualTo("010-1234-1234");
		}

		@Test
		@DisplayName("아이디가 중복되면 member 생성 실패")
		public void fail1_saveMember() {

			// when
			when(memberRepository.existsByUsername(anyString())).thenReturn(true);

			// then
			assertThatThrownBy(() -> memberService.saveMember(request))
					.isInstanceOf(DuplicateUsernameException.class)
					.hasMessage("이미 존재하는 아이디입니다.");
		}

		@Test
		@DisplayName("이메일이 중복되면 member 생성 실패")
		public void fail2_saveMember() {

			// when
			when(memberRepository.existsByEmail(anyString())).thenReturn(true);

			// then
			assertThatThrownBy(() -> memberService.saveMember(request))
					.isInstanceOf(DuplicateEmailException.class)
					.hasMessage("이미 사용중인 이메일입니다.");
		}
	}

	@Nested
	@DisplayName("findOne 메서드는")
	class FindOneCase {

		@Test
		@DisplayName("member 단건 조회 성공")
		public void success_findOne() {

			// when
			when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

			// then
			MemberFindResponse result = memberService.findOne(1L);

			assertThat(result.getId()).isEqualTo(1L);
			assertThat(result.getUsername()).isEqualTo("홍길동");
			assertThat(result.getEmail()).isEqualTo("naver@naver.com");
			assertThat(result.getPhoneNumber()).isEqualTo("010-1234-1234");
		}

		@Test
		@DisplayName("존재하지 않는 id 를 조회할때 member 단건 조회 실패")
		public void fail1_findOne() {

			// when
			when(memberRepository.findById(-1L)).thenThrow(NoSuchMemberException.class);

			// then
			assertThatThrownBy(() -> memberService.findOne(-1L))
					.isInstanceOf(NoSuchMemberException.class)
					.hasMessage("존재하지 않는 회원입니다.");
		}
	}

	@Nested
	@DisplayName("update 메서드는")
	class UpdateCase {

		// given
		MemberUpdateRequest request = new MemberUpdateRequest(
				"유재석", "gmail@naver.com","010-9999-1234");
		@Test
		@DisplayName("member 수정 성공")
		public void success_update() {

			// when
			when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
			when(memberRepository.existsByUsername(anyString())).thenReturn(false);
			when(memberRepository.existsByEmail(anyString())).thenReturn(false);

			// then
			memberService.update(1L, request);
			MemberFindResponse result = memberService.findOne(1L);

			assertThat(result.getUsername()).isEqualTo("유재석");
			assertThat(result.getEmail()).isEqualTo("gmail@naver.com");
			assertThat(result.getPhoneNumber()).isEqualTo("010-9999-1234");
		}

		@Test
		@DisplayName("존재하지 않은 id 를 조회했을때 수정 실패")
		public void fail1_update() {

			// when
			when(memberRepository.findById(-1L)).thenThrow(NoSuchMemberException.class);

			// then
			assertThatThrownBy(() -> memberService.update(-1L, request))
					.isInstanceOf(NoSuchMemberException.class)
					.hasMessage("존재하지 않는 회원입니다.");
		}

		@Test
		@DisplayName("변경한 아이디가 중복된 아이디일때 수정 실패")
		public void fail2_update() {

			// when
			when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
			when(memberRepository.existsByUsername(anyString())).thenReturn(true);

			// then
			assertThatThrownBy(() -> memberService.update(1L, request))
					.isInstanceOf(DuplicateUsernameException.class)
					.hasMessage("이미 존재하는 아이디입니다.");
		}

		@Test
		@DisplayName("변경한 이메일이 중복된 이메일일때 수정 실패")
		public void fail3_update() {

			// when
			when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
			when(memberRepository.existsByEmail(anyString())).thenReturn(true);

			// then
			assertThatThrownBy(() -> memberService.update(1L, request))
					.isInstanceOf(DuplicateEmailException.class)
					.hasMessage("이미 사용중인 이메일입니다.");
		}
	}

	@Nested
	@DisplayName("delete 메서드는")
	class DeleteCase {

		@Test
		@DisplayName("member 삭제 성공")
		public void success_delete() {

			// when
			when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
			when(restaurantRepository.existsByMember(member)).thenReturn(false);

			// then
			memberService.delete(1L);

			verify(reviewRepository, times(1)).deleteAllByMember(member);
			verify(commentRepository, times(1)).deleteAllByMember(member);
			verify(replyRepository, times(1)).deleteAllByMember(member);
			verify(memberRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("존재하지 않는 id 를 삭제할때 member 삭제 실패")
		public void fail1_delete() {

			// when
			when(memberRepository.findById(1L)).thenThrow(NoSuchMemberException.class);

			// then
			assertThatThrownBy(() -> memberService.delete(1L))
					.isInstanceOf(NoSuchMemberException.class)
					.hasMessage("존재하지 않는 회원입니다.");
		}

		@Test
		@DisplayName("회원이 식당을 가지고 있을때 member 삭제 실패")
		public void fail2_delete() {

			// when
			when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
			when(restaurantRepository.existsByMember(member)).thenReturn(true);

			// then
			assertThatThrownBy(() -> memberService.delete(1L))
					.isInstanceOf(ConstraintViolationMemberException.class)
					.hasMessage("회원탈퇴 이전에 보유하신 식당을 삭제해주세요.");
		}
	}

}
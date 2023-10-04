package com.home.whattoeat.service;

import com.home.whattoeat.config.auth.PrincipalDetails;
import com.home.whattoeat.config.jwt.JwtTokenUtil;
import com.home.whattoeat.dto.member.MemberLoginRequest;
import com.home.whattoeat.dto.member.MemberFindResponse;
import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberSaveResponse;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.dto.member.TokenResponse;
import com.home.whattoeat.exception.member.ConstraintViolationMemberException;
import com.home.whattoeat.exception.member.DuplicateEmailException;
import com.home.whattoeat.exception.member.DuplicateUsernameException;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import com.home.whattoeat.repository.CommentRepository;
import com.home.whattoeat.repository.MemberRepository;
import com.home.whattoeat.repository.ReplyRepository;
import com.home.whattoeat.repository.ReviewRepository;
import com.home.whattoeat.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final RestaurantRepository restaurantRepository;
	private final ReviewRepository reviewRepository;
	private final CommentRepository commentRepository;
	private final ReplyRepository replyRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenUtil jwtTokenUtil;

	// 회원가입
	@Transactional
	public MemberSaveResponse saveMember(MemberSaveRequest request) {

		// 이미 가입된 회원인지 중복체크
		isDuplicatedEmail(request.getEmail());
		isDuplicatedUsername(request.getUsername());

		// 이메일 형식 체크해야하나, 전화번호 형식 체크 어디서해
		// 패스워드 암호화
		String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
		Member member = Member.createMember(request, encodedPassword);

		Member saveMember = memberRepository.save(member);
		return MemberSaveResponse.form(saveMember);
	}


	// 로그인
	public TokenResponse login(MemberLoginRequest memberLoginRequest) {

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(memberLoginRequest.getUsername(), memberLoginRequest.getPassword());

		System.out.println("memberService-login authenticationManagerBuilder 로 실제 인증을 시작합니다.");
		// 실제 인증 - DaoAuthenticationProvider class 내 additionalAuthenticationChecks() 메소드로 비밀번호 체크
		Authentication authentication = authenticationManagerBuilder.getObject()
				.authenticate(authenticationToken);

		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		// 엑세스 토큰 생성
		TokenResponse tokenResponse = jwtTokenUtil.createToken(principalDetails);

		return tokenResponse;

	}

	// 이용 x - 모든 회원 조회
	public Page<MemberFindResponse> findAll(Pageable pageable) {
		return memberRepository.findAll(pageable).map(MemberFindResponse::from);
	}

	// 이용 x - 단건 조회
	public MemberFindResponse findOne(Long id) {
		Member findMember = memberRepository.findById(id)
				.orElseThrow(NoSuchMemberException::new);
		return MemberFindResponse.from(findMember);
	}

	// 회원 수정
	@Transactional
	public void update(Long id, MemberUpdateRequest request) {
		Member findMember = memberRepository.findById(id)
				.orElseThrow(NoSuchMemberException::new);

		// 변경하고자하는 이름이 중복된 이름일때
		if (!findMember.getUsername().equals(request.getUsername())) {
			isDuplicatedUsername(request.getUsername());
		}
		// 변경하고자하는 이메일이 중복된 이메일일때
		if (!findMember.getEmail().equals(request.getEmail())) {
			isDuplicatedEmail(request.getEmail());
		}

		findMember.update(request);
	}

	// 회원 삭제
	@Transactional
	public void delete(Long id) {
		Member findMember = memberRepository.findById(id)
				.orElseThrow(NoSuchMemberException::new);

		// 진행중인 주문이 있는지(사장님과의 상호작용이 필요하기 때문에 현재는 주문이 생성되면 주문이 마무리된것으로 취급)
		// 회원탈퇴하기 전 보유한 식당을 삭제해야함
		boolean existsByMember = restaurantRepository.existsByMember(findMember);
		if (existsByMember) throw new ConstraintViolationMemberException();

		// 회원이 작성한 리뷰나 댓글 대댓글은 삭제
		reviewRepository.deleteAllByMember(findMember);
		commentRepository.deleteAllByMember(findMember);
		replyRepository.deleteAllByMember(findMember);

		memberRepository.deleteById(id);
	}

	private void isDuplicatedUsername(String username) {
		boolean findUsername = memberRepository.existsByUsername(username);
		if (findUsername) throw new DuplicateUsernameException();
	}

	private void isDuplicatedEmail(String email) {
		boolean findEmail = memberRepository.existsByEmail(email);
		if (findEmail) throw new DuplicateEmailException();
	}

}


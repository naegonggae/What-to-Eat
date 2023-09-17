package com.home.whattoeat.service;

import com.home.whattoeat.dto.member.MemberFindAllResponse;
import com.home.whattoeat.dto.member.MemberFindOneResponse;
import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberSaveResponse;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import com.home.whattoeat.domain.Member;
import com.home.whattoeat.exception.member.DuplicateEmailException;
import com.home.whattoeat.exception.member.DuplicateUsernameException;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import com.home.whattoeat.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	EntityManager em;

	// 회원가입
	@Transactional
	public MemberSaveResponse saveMember(MemberSaveRequest request) {

		// 이미 가입된 회원인지 확인
		boolean findEmail = memberRepository.existsByEmail(request.getEmail());
		if (findEmail) throw new DuplicateEmailException();
		boolean findUsername = memberRepository.existsByUsername(request.getUsername());
		if (findUsername) throw new DuplicateUsernameException();

		// 이메일 형식 체크해야하나
		// 저장 근데 member 저장할때는 받을 수 있는건 다 입력받아야하는거 아님?
		// 패스워드 암호화
		String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
		Member member = Member.builder()
				.username(request.getUsername())
				.password(encodedPassword)
				.email(request.getEmail())
				.build();

		Member saveMember = memberRepository.save(member);
		return MemberSaveResponse.form(saveMember);
	}

	// 전체 조회
	public Page<MemberFindAllResponse> findAll(Pageable pageable) {
		return memberRepository.findAll(pageable).map(MemberFindAllResponse::from);
	}

	// 단건 조회
	public MemberFindOneResponse findOne(Long id) {
		Member findMember = memberRepository.findById(id)
				.orElseThrow(NoSuchMemberException::new);
		return MemberFindOneResponse.from(findMember);
	}

	// 회원 수정
	@Transactional
	public void update(Long id, MemberUpdateRequest request) {
		Member findMember = memberRepository.findById(id)
				.orElseThrow(NoSuchMemberException::new);

		// 이름이 변경되었으면 다른 이름들이랑 중복체크해줘
		if (!findMember.getUsername().equals(request.getUsername())) {
			boolean findUsername = memberRepository.existsByUsername(request.getUsername());
			if (findUsername) throw new DuplicateUsernameException();
		}
		// 이메일이 변경되었으면 다른 이메일들이랑 중복체크해줘
		if (!findMember.getEmail().equals(request.getEmail())) {
			boolean findEmail = memberRepository.existsByEmail(request.getEmail());
			if (findEmail) throw new DuplicateEmailException();
		}

		findMember.update(request);
	}

	// 회원 삭제
	@Transactional
	public void delete(Long id) {
		Member findMember = memberRepository.findById(id)
				.orElseThrow(NoSuchMemberException::new);

		// 여기에 삭제하기 전에 날짜를 표시할건지 아니면 배송중인 음식이 있는지 등 확인
		// 음식점과 메뉴들이 삭제 되는데 괜찮냐
		// 진행중인 배송이 있는 체크
		// 이벤트 체크

		// 직접삭제
//		memberRepository.delete(findMember);

		// 소프트 삭제
		findMember.softDelete();
//		memberRepository.saveAndFlush(findMember);
//		em.flush();
//		em.clear();
//		if (em.contains(findMember)) {
//			// 엔티티가 영속성 컨텍스트에 존재하는 경우
////			em.evict(findMember); // 영속성 컨텍스트에서 제거
//			System.out.println("아직 캐시에 남아있소");
//		}
//		Member member = memberRepository.findById(findMember.getId()).orElseThrow();
//		System.out.println(member.getDeletedAt());
	}

	// 비밀번호 변경
	// 로그인?
}


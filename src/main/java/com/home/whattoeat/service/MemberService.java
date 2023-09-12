package com.home.whattoeat.service;

import com.home.whattoeat.dto.member.MemberFindAllResponse;
import com.home.whattoeat.dto.member.MemberFindOneResponse;
import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberSaveResponse;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import com.home.whattoeat.entity.Member;
import com.home.whattoeat.exception.member.DuplicateEmailException;
import com.home.whattoeat.exception.member.DuplicateUsernameException;
import com.home.whattoeat.exception.member.NoSuchMemberException;
import com.home.whattoeat.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

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
		Member member = Member.builder()
				.username(request.getUsername())
				.password(request.getPassword())
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
		memberRepository.delete(findMember);
	}

	// 비밀번호 변경
	// 로그인?
}


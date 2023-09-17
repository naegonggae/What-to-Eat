package com.home.whattoeat.service;

import com.home.whattoeat.domain.Member;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import com.home.whattoeat.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberServiceSpringBootTest { // 실험용 테스트

	@Autowired
	EntityManager em;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	MemberService memberService;

	@Test
	@DisplayName("엔티티가 생성, 수정, 삭제 될때 시간들은 각각 어떻게 될까")
	public void date() throws Exception {
		Member member = Member.builder().id(1L).username("홍길동").email("email@naver.com").password("1234").build();
		System.out.println("==============");

		Member savedMember = memberRepository.save(member);
		System.out.println("==============");

//		System.out.println("삭제날짜 "+savedMember.getDeletedAt());
//		System.out.println("생성날짜 "+savedMember.getCreatedAt());
//		System.out.println("수정날짜 "+savedMember.getUpdatedAt());

		em.flush();
		em.clear();
		System.out.println("==============");


		MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest("이상훈", "hi@naver.com");
		memberService.update(1L, memberUpdateRequest);
		Member findMember = memberRepository.findById(1L).orElseThrow();
		System.out.println("삭제날짜 "+findMember.getDeletedAt());
		System.out.println("생성날짜 "+findMember.getCreatedAt());
		System.out.println("수정날짜 "+findMember.getUpdatedAt());

		em.flush();
		em.clear();
		System.out.println("==============");


		// 삭제하면 삭제시점 저장되고 생성시간 수정시간 null 됨 왜...? 진짜 삭제해버리니까...
		memberService.delete(1L);
		Member findMember2 = memberRepository.findById(1L).orElseThrow();

		System.out.println("삭제날짜 "+findMember2.getDeletedAt());
		System.out.println("생성날짜 "+findMember2.getCreatedAt());
		System.out.println("수정날짜 "+findMember2.getUpdatedAt());
		System.out.println("member 정보 "+ memberRepository.findById(1L).orElseThrow()); // BaseEntity 에 Where 풀고 해야돼

	}

}
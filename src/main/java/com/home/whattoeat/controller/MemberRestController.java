package com.home.whattoeat.controller;

import com.home.whattoeat.dto.member.MemberFindAllResponse;
import com.home.whattoeat.dto.member.MemberFindOneResponse;
import com.home.whattoeat.dto.member.MemberSaveRequest;
import com.home.whattoeat.dto.member.MemberSaveResponse;
import com.home.whattoeat.dto.member.MemberUpdateRequest;
import com.home.whattoeat.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberRestController {

	private final MemberService memberService;

	@GetMapping("/{id}")
	public ResponseEntity<MemberFindOneResponse> findOne(@PathVariable Long id) {
		MemberFindOneResponse findMember = memberService.findOne(id);
		return ResponseEntity.ok().body(findMember);
	}

	@GetMapping()
	public ResponseEntity<Page<MemberFindAllResponse>> findAll(Pageable pageable) {
		Page<MemberFindAllResponse> memberList = memberService.findAll(pageable);
		return ResponseEntity.ok().body(memberList);
	}

	@PostMapping()
	public ResponseEntity<MemberSaveResponse> save(@RequestBody MemberSaveRequest request) {
		MemberSaveResponse memberSaveResponse = memberService.saveMember(request);
		return ResponseEntity.ok().body(memberSaveResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MemberUpdateRequest request) {
		memberService.update(id, request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		memberService.delete(id);
		return ResponseEntity.noContent().build();
	}

}

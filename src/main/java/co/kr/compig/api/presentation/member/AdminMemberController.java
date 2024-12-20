package co.kr.compig.api.presentation.member;

import java.util.List;
import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.presentation.member.request.AdminMemberCreate;
import co.kr.compig.api.presentation.member.request.AdminMemberUpdate;
import co.kr.compig.api.presentation.member.request.GuardianMemberCreate;
import co.kr.compig.api.presentation.member.request.GuardianMemberUpdate;
import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.request.NoMemberCreate;
import co.kr.compig.api.presentation.member.request.PartnerMemberCreate;
import co.kr.compig.api.presentation.member.request.PartnerMemberUpdate;
import co.kr.compig.api.presentation.member.response.GuardianMemberResponse;
import co.kr.compig.api.presentation.member.response.MemberResponse;
import co.kr.compig.api.presentation.member.response.PartnerMemberResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import co.kr.compig.global.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 멤버", description = "멤버 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/members", produces = "application/json")
public class AdminMemberController {

	private final MemberService memberService;

	@Operation(summary = "최종 접속 일시, 접속 ip 업데이트")
	@PutMapping("/recent-login")
	public ResponseEntity<Response<?>> updateRecentLogin() {
		memberService.updateRecentLogin(SecurityUtil.getMemberId());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "비회원 회원가입")
	@PostMapping("/no")
	public ResponseEntity<Response<?>> noMemberCreate(@RequestBody @Valid NoMemberCreate noMemberCreate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.noMemberCreate(noMemberCreate)))
			.build());
	}

	@Operation(summary = "관리자 일반 회원가입")
	@PostMapping
	public ResponseEntity<Response<?>> adminCreate(
		@RequestBody @Valid AdminMemberCreate adminMemberCreate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.adminCreate(adminMemberCreate)))
			.build());
	}

	@Operation(summary = "보호자 회원가입")
	@PostMapping(path = "/guardians")
	public ResponseEntity<Response<?>> guardianCreate(
		@RequestBody @Valid GuardianMemberCreate guardianMemberCreate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.guardianCreate(guardianMemberCreate)))
			.build());
	}

	@Operation(summary = "간병인 회원가입")
	@PostMapping(path = "/partners")
	public ResponseEntity<Response<?>> partnerCreate(
		@RequestBody @Valid PartnerMemberCreate partnerMemberCreate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.partnerCreate(partnerMemberCreate)))
			.build());
	}

	@Operation(summary = "관리자 리스트", description = "페이징")
	@GetMapping
	public ResponseEntity<PageResponse> getAdminPage(
		@ParameterObject @ModelAttribute MemberSearchRequest memberSearchRequest) {
		Page<MemberResponse> page = memberService.getAdminPage(memberSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Operation(summary = "관리자 memberId 조회")
	@GetMapping("/{memberId}")
	public ResponseEntity<MemberResponse> getAdminByMemberId(@PathVariable String memberId) {
		return ResponseEntity.ok(memberService.getMemberResponseByMemberId(memberId));
	}

	@Operation(summary = "보호자 리스트", description = "페이징")
	@GetMapping(path = "/guardians")
	public ResponseEntity<PageResponse> getGuardianPage(
		@ParameterObject @ModelAttribute MemberSearchRequest memberSearchRequest) {
		Page<GuardianMemberResponse> page = memberService.getGuardianPage(memberSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Operation(summary = "유저 리스트 main 에서 검색", description = "페이징 없음")
	@GetMapping(path = "/main")
	public ResponseEntity<List<MemberResponse>> getGuardianList(
		@ParameterObject @ModelAttribute MemberSearchRequest memberSearchRequest) {
		return ResponseEntity.ok(memberService.getUserList(memberSearchRequest));
	}

	@Operation(summary = "보호자 memberId 조회")
	@GetMapping("/guardians/{memberId}")
	public ResponseEntity<GuardianMemberResponse> getGuardianByMemberId(@PathVariable String memberId) {
		return ResponseEntity.ok(memberService.getGuardianMemberResponseByMemberId(memberId));
	}

	@Operation(summary = "간병인 리스트", description = "페이징")
	@GetMapping(path = "/partners")
	public ResponseEntity<PageResponse> getPartnerPage(
		@ParameterObject @ModelAttribute MemberSearchRequest memberSearchRequest) {
		Page<PartnerMemberResponse> page = memberService.getPartnerPage(memberSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Operation(summary = "간병인 memberId 조회")
	@GetMapping("/partners/{memberId}")
	public ResponseEntity<PartnerMemberResponse> getPartnerByMemberId(@PathVariable String memberId) {
		return ResponseEntity.ok(memberService.getPartnerMemberResponseByMemberId(memberId));
	}

	@Operation(summary = "관리자 memberId 수정")
	@PutMapping("/{memberId}")
	public ResponseEntity<Response<?>> updateAdminById(@PathVariable String memberId,
		@RequestBody @Valid AdminMemberUpdate adminMemberUpdate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.updateAdminById(memberId, adminMemberUpdate)))
			.build());
	}

	@Operation(summary = "간병인 memberId 수정")
	@PutMapping("/partners/{memberId}")
	public ResponseEntity<Response<?>> updatePartnerById(@PathVariable String memberId,
		@RequestBody @Valid PartnerMemberUpdate partnerMemberUpdate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.updatePartnerById(memberId, partnerMemberUpdate)))
			.build());
	}

	@Operation(summary = "보호자 memberId 수정")
	@PutMapping("/guardians/{memberId}")
	public ResponseEntity<Response<?>> updateGuardianById(@PathVariable String memberId,
		@RequestBody @Valid GuardianMemberUpdate guardianMemberUpdate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.updateGuardianById(memberId, guardianMemberUpdate)))
			.build());
	}

	@Operation(summary = "관리자 탈퇴, 보호자 탈퇴, 간병인 탈퇴")
	@PutMapping("/{memberId}/leave")
	public ResponseEntity<Response<?>> updateGuardianById(@PathVariable String memberId) {
		memberService.doUserLeave(memberId);
		return ResponseEntity.ok().build();
	}

}

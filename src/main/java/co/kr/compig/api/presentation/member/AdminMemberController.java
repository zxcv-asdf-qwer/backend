package co.kr.compig.api.presentation.member;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.presentation.member.request.AdminMemberCreate;
import co.kr.compig.api.presentation.member.request.GuardianMemberCreate;
import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.request.PartnerMemberCreate;
import co.kr.compig.api.presentation.member.response.AdminMemberResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
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
		@RequestPart @Valid PartnerMemberCreate partnerMemberCreate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.partnerCreate(partnerMemberCreate)))
			.build());
	}

	@Operation(summary = "관리자 리스트", description = "페이징")
	@GetMapping
	public ResponseEntity<PageResponse<AdminMemberResponse>> getAdminPage(
		@ParameterObject @RequestParam(required = false) @Valid MemberSearchRequest memberSearchRequest,
		@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(memberService.getAdminPage(memberSearchRequest, pageable));
	}

	@Operation(summary = "관리자 memberId 조회")
	@GetMapping("/{memberId}")
	public ResponseEntity<AdminMemberResponse> getAdminByMemberId(@PathVariable String memberId) {
		return ResponseEntity.ok(memberService.getMemberById(memberId).toAdminMemberResponse());
	}

}

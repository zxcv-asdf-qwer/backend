package co.kr.compig.api.presentation.member;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.presentation.member.request.AdminMemberCreate;
import co.kr.compig.api.presentation.member.request.GuardianMemberCreate;
import co.kr.compig.api.presentation.member.request.PartnerMemberCreate;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "일반 회원가입", description = "일반 회원가입 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/members", produces = "application/json")
public class MemberController {

	private final MemberService memberService;

	@Operation(summary = "관리자 회원가입")
	@PostMapping(path = "/admin")
	public ResponseEntity<Response<?>> adminCreate(
		@ModelAttribute @Valid AdminMemberCreate adminMemberCreate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.adminCreate(adminMemberCreate)))
			.build());
	}

	@Operation(summary = "보호자 회원가입")
	@PostMapping(path = "/guardian")
	public ResponseEntity<Response<?>> guardianCreate(
		@ModelAttribute @Valid GuardianMemberCreate guardianMemberCreate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.guardianCreate(guardianMemberCreate)))
			.build());
	}

	@Operation(summary = "간병인 회원가입")
	@PostMapping(path = "/partner")
	public ResponseEntity<Response<?>> partnerCreate(
		@ModelAttribute @Valid PartnerMemberCreate partnerMemberCreate) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("memberId", memberService.partnerCreate(partnerMemberCreate)))
			.build());
	}

	@Operation(summary = "이메일 중복")
	@GetMapping(path = "/emails/availability")
	public ResponseEntity<Response<?>> availabilityEmail(
		@RequestParam("userEmail") String userEmail) {
		memberService.availabilityEmail(userEmail);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "이메일 찾기")
	@GetMapping(path = "/emails")
	public ResponseEntity<Response<?>> findEmail(@RequestParam("userNm") String userNm,
		@RequestParam("userTel") String userTel) {
		return ResponseEntity.ok(Response.<Map<String, String>>builder()
			.data(Map.of("userId", memberService.findEmail(userNm, userTel)))
			.build());
	}
}

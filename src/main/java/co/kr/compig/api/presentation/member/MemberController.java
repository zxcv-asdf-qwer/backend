package co.kr.compig.api.presentation.member;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "회원", description = "회원 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/members", produces = "application/json")
public class MemberController {

	private final MemberService memberService;

	@Operation(summary = "이메일 중복", hidden = true)
	@GetMapping(path = "/emails/availability")
	public ResponseEntity<Response<?>> availabilityEmail(
		@RequestParam("userEmail") String userEmail) {
		memberService.availabilityEmail(userEmail);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "이메일 찾기", hidden = true)
	@GetMapping(path = "/emails")
	public ResponseEntity<Response<?>> findEmail(@RequestParam("userNm") String userNm,
		@RequestParam("userTel") String userTel) {
		return ResponseEntity.ok(Response.<Map<String, String>>builder()
			.data(Map.of("userId", memberService.findEmail(userNm, userTel)))
			.build());
	}

	@Operation(summary = "인증 번호 생성하기")
	@PostMapping(path = "/authentication/{receiverPhoneNumber}")
	public ResponseEntity<Response<?>> sendAuthentication(
		@PathVariable(name = "receiverPhoneNumber") String receiverPhoneNumber) {
		memberService.sendSmsAuthentication(String.valueOf(receiverPhoneNumber));
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "인증 상태 조회")
	@GetMapping(path = "/authentication/{receiverPhoneNumber}/{authenticationNumber}")
	public ResponseEntity<Response<?>> getAuthentication(
		@PathVariable(name = "receiverPhoneNumber") String receiverPhoneNumber,
		@PathVariable(name = "authenticationNumber") String authenticationNumber) {
		memberService.getAuthentication(receiverPhoneNumber, authenticationNumber);
		return ResponseEntity.ok().build();
	}
}

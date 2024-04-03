package co.kr.compig.api.presentation.member;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(path = "/members", produces = "application/json")
public class MemberController {

	private final MemberService memberService;

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

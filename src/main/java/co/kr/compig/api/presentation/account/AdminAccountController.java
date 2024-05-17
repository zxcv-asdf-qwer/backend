package co.kr.compig.api.presentation.account;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.account.AccountService;
import co.kr.compig.api.presentation.account.request.AccountSaveRequest;
import co.kr.compig.api.presentation.account.response.AccountDetailResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 계좌", description = "계좌 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/account", produces = "application/json")
public class AdminAccountController {

	private final AccountService accountService;

	@Operation(summary = "상세 조회 - 멤버 id")
	@GetMapping(path = "/member/{memberId}")
	public ResponseEntity<Response<AccountDetailResponse>> getAccountByMember(
		@PathVariable(name = "memberId") String memberId) {
		return ResponseEntity.ok(
			Response.<AccountDetailResponse>builder().data(accountService.getAccountByMemberId(memberId)).build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/member/{memberId}")
	public ResponseEntity<Response<?>> deleteAccount(
		@PathVariable(name = "memberId") String memberId) {
		accountService.deleteAccount(memberId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "인증 후 저장, 기존 데이터 있으면 수정")
	@GetMapping(path = "/member/{memberId}/checkAccount")
	public ResponseEntity<Response<?>> checkAccount(@PathVariable(name = "memberId") String memberId,
		@ParameterObject @ModelAttribute AccountSaveRequest accountSaveRequest) {
		return ResponseEntity.ok()
			.body(Response.<Map<String, Long>>builder()
				.data(Map.of("accountId", accountService.getAccountCheck(memberId, accountSaveRequest)))
				.build());
	}

}

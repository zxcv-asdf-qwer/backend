package co.kr.compig.api.presentation.account;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.account.AccountService;
import co.kr.compig.api.presentation.account.request.AccountSaveRequest;
import co.kr.compig.api.presentation.account.response.AccountDetailResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "보호자 계좌", description = "계좌 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/guardian/account", produces = "application/json")
public class GuardianAccountController {

	private final AccountService accountService;

	@Operation(summary = "상세 조회 - 멤버 id")
	@GetMapping(path = "/member")
	public ResponseEntity<Response<AccountDetailResponse>> getAccountByMember() {
		return ResponseEntity.ok(Response.<AccountDetailResponse>builder()
			.data(accountService.getAccountByMemberId(SecurityUtil.getMemberId()))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping
	public ResponseEntity<Response<?>> deleteAccount() {
		accountService.deleteAccount(SecurityUtil.getMemberId());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "인증 후 저장, 기존 데이터 있으면 수정")
	@GetMapping(path = "/checkAccount")
	public ResponseEntity<Response<?>> checkAccount(
		@ParameterObject @ModelAttribute AccountSaveRequest accountSaveRequest) {
		return ResponseEntity.ok()
			.body(Response.<Map<String, Long>>builder()
				.data(
					Map.of("accountId", accountService.getAccountCheck(SecurityUtil.getMemberId(), accountSaveRequest)))
				.build());
	}

}

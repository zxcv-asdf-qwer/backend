package co.kr.compig.api.presentation.account;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.presentation.account.dto.request.AccountCheckRequest;
import co.kr.compig.api.presentation.account.dto.response.AccountCheckResponse;
import co.kr.compig.api.presentation.account.dto.request.AccountCreateRequest;
import co.kr.compig.api.presentation.account.dto.response.AccountDetailResponse;
import co.kr.compig.api.presentation.account.dto.request.AccountUpdateRequest;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.account.AccountCheckService;
import co.kr.compig.service.account.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/account", produces = "application/json")
public class UserAccountController {

	private final AccountService accountService;
	private final AccountCheckService accountCheckService;

	@PostMapping
	public ResponseEntity<Response<?>> createAccount(
		@ModelAttribute @Valid AccountCreateRequest accountCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("accountId", accountService.createAccount(accountCreateRequest)))
			.build());
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<Response<AccountDetailResponse>> getAccount(
		@PathVariable(name = "accountId") Long accountId) {
		return ResponseEntity.ok(Response.<AccountDetailResponse>builder()
			.data(accountService.getAccountByAccountId(accountId))
			.build());
	}

	@GetMapping("/member/{memberId}")
	public ResponseEntity<Response<AccountDetailResponse>> getAccountByMember(
		@PathVariable(name = "memberId") String memberId) {
		return ResponseEntity.ok(Response.<AccountDetailResponse>builder()
			.data(accountService.getAccountByMemberId(memberId))
			.build());
	}

	@PutMapping("/{accountId}")
	public ResponseEntity<Response<?>> updateAccount(@PathVariable(name = "accountId") Long accountId,
		@RequestBody @Valid AccountUpdateRequest accountUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("accountId", accountService.updateAccount(accountId, accountUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{accountId}")
	public ResponseEntity<Response<?>> deleteAccount(
		@PathVariable(name = "accountId") Long accountId) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("accountId", accountService.deleteAccount(accountId)))
			.build());
	}

	@GetMapping("/checkAccount")
	public ResponseEntity<Response<AccountCheckResponse>> checkAccount(@ModelAttribute
	AccountCheckRequest accountCheckRequest) {
		return ResponseEntity.ok(Response.<AccountCheckResponse>builder()
			.data(accountCheckService.getAccountCheck(accountCheckRequest))
			.build());
	}

	@GetMapping("/getAccountCheck")
	public ResponseEntity<Response<Boolean>> getAccountCheck(@RequestBody String memberId) {
		return ResponseEntity.ok(Response.<Boolean>builder()
			.data(accountService.getAccountCheck(memberId))
			.build());
	}
}

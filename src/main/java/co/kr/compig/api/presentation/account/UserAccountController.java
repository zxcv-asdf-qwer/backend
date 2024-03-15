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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.account.AccountCheckService;
import co.kr.compig.api.application.account.AccountService;
import co.kr.compig.api.presentation.account.request.AccountCheckRequest;
import co.kr.compig.api.presentation.account.request.AccountCreateRequest;
import co.kr.compig.api.presentation.account.request.AccountUpdateRequest;
import co.kr.compig.api.presentation.account.response.AccountCheckResponse;
import co.kr.compig.api.presentation.account.response.AccountDetailResponse;
import co.kr.compig.global.dto.Response;
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
		@ModelAttribute @Valid AccountCreateRequest accountCreateRequest,
		@RequestPart(value = "file", required = false) Map<String, String> files
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("accountId", accountService.createAccount(accountCreateRequest, files)))
			.build());
	}

	@GetMapping(path = "/{accountId}")
	public ResponseEntity<Response<AccountDetailResponse>> getAccount(
		@PathVariable(name = "accountId") Long accountId) {
		return ResponseEntity.ok(Response.<AccountDetailResponse>builder()
			.data(accountService.getAccountByAccountId(accountId))
			.build());
	}

	@GetMapping(path = "/member/{memberId}")
	public ResponseEntity<Response<AccountDetailResponse>> getAccountByMember(
		@PathVariable(name = "memberId") String memberId) {
		return ResponseEntity.ok(Response.<AccountDetailResponse>builder()
			.data(accountService.getAccountByMemberId(memberId))
			.build());
	}

	@PutMapping(path = "/{accountId}")
	public ResponseEntity<Response<?>> updateAccount(@PathVariable(name = "accountId") Long accountId,
		@RequestBody @Valid AccountUpdateRequest accountUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("accountId", accountService.updateAccount(accountId, accountUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{accountId}")
	public ResponseEntity<Response<?>> deleteAccount(
		@PathVariable(name = "accountId") Long accountId) {
		accountService.deleteAccount(accountId);
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/checkAccount")
	public ResponseEntity<Response<AccountCheckResponse>> checkAccount(@ModelAttribute
	AccountCheckRequest accountCheckRequest) {
		return ResponseEntity.ok(Response.<AccountCheckResponse>builder()
			.data(accountCheckService.getAccountCheck(accountCheckRequest))
			.build());
	}

	@GetMapping(path = "/getAccountCheck")
	public ResponseEntity<Response<Boolean>> getAccountCheck(@RequestBody String memberId) {
		return ResponseEntity.ok(Response.<Boolean>builder()
			.data(accountService.getAccountCheck(memberId))
			.build());
	}
}

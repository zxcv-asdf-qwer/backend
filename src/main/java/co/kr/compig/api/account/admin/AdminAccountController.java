package co.kr.compig.api.account.admin;

import co.kr.compig.api.account.dto.AccountCreateRequest;
import co.kr.compig.api.account.dto.AccountDetailResponse;
import co.kr.compig.api.account.dto.AccountUpdateRequest;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.account.AccountService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/account", produces = "application/json")
public class AdminAccountController {

  private final AccountService accountService;

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
        .data(accountService.getAccount(accountId))
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
}

package co.kr.compig.api.presentation.wallet;

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

import co.kr.compig.api.application.wallet.WalletService;
import co.kr.compig.api.presentation.wallet.request.WalletCreateRequest;
import co.kr.compig.api.presentation.wallet.request.WalletUpdateRequest;
import co.kr.compig.api.presentation.wallet.response.WalletDetailResponse;
import co.kr.compig.global.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/wallet", produces = "application/json")
public class UserWalletController {
	private final WalletService walletService;

	@PostMapping
	public ResponseEntity<Response<?>> createWallet(
		@ModelAttribute @Valid WalletCreateRequest walletCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("walletId", walletService.createWallet(walletCreateRequest)))
			.build());
	}

	@GetMapping(path = "/{walletId}")
	public ResponseEntity<Response<WalletDetailResponse>> getWallet(
		@PathVariable(name = "walletId") Long walletId
	) {
		return ResponseEntity.ok(Response.<WalletDetailResponse>builder()
			.data(walletService.getWallet(walletId))
			.build());
	}

	@PutMapping(path = "/{walletId}")
	public ResponseEntity<Response<?>> updateWallet(@PathVariable(name = "walletId") Long walletId,
		@RequestBody @Valid WalletUpdateRequest walletUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("walletId", walletService.updateWallet(walletId, walletUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{walletId}")
	public ResponseEntity<Response<?>> deleteWallet(@PathVariable(name = "walletId") Long walletId) {
		walletService.deleteWallet(walletId);
		return ResponseEntity.ok().build();
	}
}

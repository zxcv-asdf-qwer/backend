package co.kr.compig.api.presentation.wallet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.wallet.WalletService;
import co.kr.compig.api.presentation.wallet.response.WalletResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "간병인 지갑", description = "지갑 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/partner/wallet", produces = "application/json")
public class PartnerWalletController {
	private final WalletService walletService;

	@Operation(summary = "조회")
	@GetMapping(path = "/members/{memberId}")
	public ResponseEntity<Response<WalletResponse>> getWallet(@PathVariable(name = "memberId") String memberId) {
		return ResponseEntity.ok(Response.<WalletResponse>builder()
			.data(walletService.getWallet(memberId))
			.build());
	}
}

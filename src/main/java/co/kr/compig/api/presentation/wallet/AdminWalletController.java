package co.kr.compig.api.presentation.wallet;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.wallet.WalletService;
import co.kr.compig.api.presentation.wallet.request.WalletCreateRequest;
import co.kr.compig.api.presentation.wallet.request.WalletSearchRequest;
import co.kr.compig.api.presentation.wallet.response.WalletDetailResponse;
import co.kr.compig.api.presentation.wallet.response.WalletResponse;
import co.kr.compig.api.presentation.wallet.response.WalletResponseWithSecret;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 지갑", description = "지갑 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/wallet", produces = "application/json")
public class AdminWalletController {

	private final WalletService walletService;

	@Operation(summary = "수기정산")
	@PostMapping
	public ResponseEntity<Response<?>> createWallet(
		@RequestBody @Valid WalletCreateRequest walletCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("walletId", walletService.createWalletAdmin(walletCreateRequest)))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<PageResponse> getWalletPage(
		@ParameterObject @ModelAttribute WalletSearchRequest walletSearchRequest) {
		Page<WalletResponse> page = walletService.getWalletPage(walletSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());

	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/members/{memberId}")
	public ResponseEntity<Response<WalletResponse>> getWallet(@PathVariable(name = "memberId") String memberId) {
		return ResponseEntity.ok(Response.<WalletResponse>builder()
			.data(walletService.getWallet(memberId))
			.build());
	}

	@Operation(summary = "수기 정산 내역")
	@GetMapping("/exchange-hand")
	public ResponseEntity<PageResponse> getExchangeHandWalletPage(
		@ParameterObject @ModelAttribute WalletSearchRequest walletSearchRequest) {
		Page<WalletDetailResponse> page = walletService.getExchangeHandWalletPage(walletSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Operation(summary = "간병인 일일 정산내역")
	@GetMapping("/exchange-one-day")
	public ResponseEntity<PageResponse> getExchangeOneDayWalletPage(
		@ParameterObject @ModelAttribute WalletSearchRequest walletSearchRequest) {
		Page<WalletResponseWithSecret> page = walletService.getExchangeOneDayWalletPage(walletSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}
}

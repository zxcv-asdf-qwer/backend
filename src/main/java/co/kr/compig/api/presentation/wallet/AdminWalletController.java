package co.kr.compig.api.presentation.wallet;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import co.kr.compig.api.presentation.wallet.request.WalletSearchRequest;
import co.kr.compig.api.presentation.wallet.request.WalletUpdateRequest;
import co.kr.compig.api.presentation.wallet.response.WalletDetailResponse;
import co.kr.compig.api.presentation.wallet.response.WalletResponse;
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

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createWallet(
		@ParameterObject @ModelAttribute @Valid WalletCreateRequest walletCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("walletId", walletService.createWallet(walletCreateRequest)))
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
	@GetMapping(path = "/{walletId}")
	public ResponseEntity<Response<WalletDetailResponse>> getWallet(
		@PathVariable(name = "walletId") Long walletId) {
		return ResponseEntity.ok(Response.<WalletDetailResponse>builder()
			.data(walletService.getWallet(walletId))
			.build());
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping(path = "/{walletId}")
	public ResponseEntity<Response<?>> updateWallet(
		@PathVariable(name = "walletId") Long walletId,
		@RequestBody @Valid WalletUpdateRequest walletUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("walletId", walletService.updateWallet(walletId, walletUpdateRequest)))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{walletId}")
	public ResponseEntity<Response<?>> deleteWallet(
		@PathVariable(name = "walletId") Long walletId) {
		walletService.deleteWallet(walletId);
		return ResponseEntity.ok().build();
	}
}

package co.kr.compig.api.presentation.payment;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.payment.PaymentService;
import co.kr.compig.api.presentation.payment.request.PaymentSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentDetailResponse;
import co.kr.compig.api.presentation.payment.response.PaymentResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 결제", description = "결제 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/payments", produces = "application/json")
public class AdminPaymentController {

	private final PaymentService paymentService;

	@Operation(summary = "생성하기")
	@PostMapping("/orderId/{orderId}")
	public ResponseEntity<Response<?>> createPayment(@PathVariable(name = "orderId") Long orderId) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("orderUrl", paymentService.createPayment(orderId)))
			.build());
	}

	@Operation(summary = "조회", description = "페이징")
	@GetMapping
	public ResponseEntity<PageResponse> getPaymentPage(
		@ParameterObject @ModelAttribute PaymentSearchRequest paymentSearchRequest) {
		Page<PaymentResponse> page = paymentService.getPaymentPage(paymentSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{paymentId}")
	public ResponseEntity<Response<PaymentDetailResponse>> getPayment(
		@PathVariable(name = "paymentId") Long paymentId) {
		return ResponseEntity.ok(Response.<PaymentDetailResponse>builder()
			.data(paymentService.getPayment(paymentId))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{paymentId}")
	public ResponseEntity<Response<?>> deletePayment(
		@PathVariable(name = "paymentId") Long paymentId) {
		paymentService.deletePayment(paymentId);
		return ResponseEntity.ok().build();
	}

	// @Operation(summary = "간병인 일일 정산내역 WalletResponseWithSecret")
	// @GetMapping("/exchange-one-day")
	// public ResponseEntity<PageResponse> getExchangeOneDayWalletPage(
	// 	@ParameterObject @ModelAttribute WalletSearchRequest walletSearchRequest) {
	// 	return walletService.getExchangeOneDayWalletPage(walletSearchRequest);
	// }
}

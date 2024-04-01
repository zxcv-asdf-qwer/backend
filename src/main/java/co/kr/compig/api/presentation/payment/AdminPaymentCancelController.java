package co.kr.compig.api.presentation.payment;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.payment.PaymentCancelService;
import co.kr.compig.api.presentation.payment.request.PaymentCancelCreateRequest;
import co.kr.compig.api.presentation.payment.request.PaymentCancelSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentCancelDetailResponse;
import co.kr.compig.api.presentation.payment.response.PaymentCancelResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 결제 취소", description = "결제 취소 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/payment-cancel", produces = "application/json")
public class AdminPaymentCancelController {

	private final PaymentCancelService paymentCancelService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createPaymentCancel(
		@ModelAttribute @Valid PaymentCancelCreateRequest paymentCancelCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("paymentCancelId", paymentCancelService.createPaymentCancel(paymentCancelCreateRequest)))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<PageResponse<PaymentCancelResponse>> pageListPaymentCancel(
		@ModelAttribute @Valid PaymentCancelSearchRequest paymentCancelSearchRequest, Pageable pageable
	) {
		Page<PaymentCancelResponse> page = paymentCancelService.pageListPaymentCancel(paymentCancelSearchRequest,
			pageable);
		PageResponse<PaymentCancelResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{paymentCancelId}")
	public ResponseEntity<Response<PaymentCancelDetailResponse>> getPaymentCancel(
		@PathVariable(name = "paymentCancelId") Long paymentCancelId
	) {
		return ResponseEntity.ok(Response.<PaymentCancelDetailResponse>builder()
			.data(paymentCancelService.getPaymentCancel(paymentCancelId))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{paymentCancelId}")
	public ResponseEntity<Response<?>> deletePaymentCancel(
		@PathVariable(name = "paymentCancelId") Long paymentCancelId) {
		paymentCancelService.deletePaymentCancel(paymentCancelId);
		return ResponseEntity.ok().build();
	}
}

package co.kr.compig.api.presentation.payment;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/payment-cancel", produces = "application/json")
public class UserPaymentCancelController {

	private final PaymentCancelService paymentCancelService;

	@PostMapping
	public ResponseEntity<Response<?>> createPaymentCancel(
		@ModelAttribute @Valid PaymentCancelCreateRequest paymentCancelCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("paymentCancelId", paymentCancelService.createPaymentCancel(paymentCancelCreateRequest)))
			.build());
	}

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

	@GetMapping(path = "/{paymentCancelId}")
	public ResponseEntity<Response<PaymentCancelDetailResponse>> getPaymentCancel(
		@PathVariable(name = "paymentCancelId") Long paymentCancelId
	) {
		return ResponseEntity.ok(Response.<PaymentCancelDetailResponse>builder()
			.data(paymentCancelService.getPaymentCancel(paymentCancelId))
			.build());
	}
}

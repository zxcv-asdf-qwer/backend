package co.kr.compig.api.presentation.payment;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.payment.PaymentService;
import co.kr.compig.api.presentation.payment.request.PaymentCreateRequest;
import co.kr.compig.api.presentation.payment.request.PaymentSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentDetailResponse;
import co.kr.compig.api.presentation.payment.response.PaymentResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "보호자 결제", description = "결제 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/guardian/payment", produces = "application/json")
public class GuardianPaymentController {

	private final PaymentService paymentService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createPayment(
		@ParameterObject @ModelAttribute @Valid PaymentCreateRequest paymentCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("paymentId", paymentService.createPayment(paymentCreateRequest)))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<SliceResponse<PaymentResponse>> pageListPayment(
		@ParameterObject @ModelAttribute @Valid PaymentSearchRequest paymentSearchRequest, Pageable pageable) {
		Slice<PaymentResponse> slice = paymentService.pageListPaymentCursor(paymentSearchRequest, pageable);
		SliceResponse<PaymentResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable,
			slice.hasNext());
		return ResponseEntity.ok(sliceResponse);
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
}

package co.kr.compig.api.presentation.payment;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.payment.PaymentService;
import co.kr.compig.api.presentation.payment.request.PaymentSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentResponse;
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
@RequestMapping(path = "/guardian/payments", produces = "application/json")
public class GuardianPaymentController {

	private final PaymentService paymentService;

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<SliceResponse<PaymentResponse>> getPaymentSlice(
		@ParameterObject @ModelAttribute @Valid PaymentSearchRequest paymentSearchRequest,
		@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(paymentService.getPaymentSlice(paymentSearchRequest, pageable));
	}

}

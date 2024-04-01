package co.kr.compig.api.presentation.payment;

import java.util.Map;

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
import co.kr.compig.api.presentation.payment.response.PaymentDetailResponse;
import co.kr.compig.global.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/pb/payment", produces = "application/json")
public class UserPaymentController {

	private final PaymentService paymentService;

	@PostMapping
	public ResponseEntity<Response<?>> createPayment(
		@ModelAttribute @Valid PaymentCreateRequest paymentCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("paymentId", paymentService.createPayment(paymentCreateRequest)))
			.build());
	}

	@GetMapping(path = "/{paymentId}")
	public ResponseEntity<Response<PaymentDetailResponse>> getPayment(
		@PathVariable(name = "paymentId") Long paymentId
	) {
		return ResponseEntity.ok(Response.<PaymentDetailResponse>builder()
			.data(paymentService.getPayment(paymentId))
			.build());
	}

	@DeleteMapping(path = "/{paymentId}")
	public ResponseEntity<Response<?>> deletePayment(@PathVariable(name = "paymentId") Long paymentId) {
		paymentService.deletePayment(paymentId);
		return ResponseEntity.ok().build();
	}
}
package co.kr.compig.api.presentation.payment;

import java.util.List;
import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.payment.PaymentService;
import co.kr.compig.api.presentation.payment.request.PaymentExchangeOneDaySearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentDetailResponse;
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

	@Operation(summary = "결제 하기")
	@PostMapping("/orders/{orderId}")
	public ResponseEntity<Response<?>> createPayment(@PathVariable(name = "orderId") Long orderId) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("orderUrl", paymentService.createPayment(orderId)))
			.build());
	}

	@Operation(summary = "orderId 로 상세 조회", description = "createdOn 결제요청일")
	@GetMapping(path = "/{orderId}")
	public ResponseEntity<Response<List<PaymentDetailResponse>>> getPaymentsByOrderId(
		@PathVariable(name = "orderId") Long orderId) {
		return ResponseEntity.ok(Response.<List<PaymentDetailResponse>>builder()
			.data(paymentService.getPaymentsByOrderId(orderId))
			.build());
	}

	@Operation(summary = "간병인 일일 정산내역", description = "페이징 PackingExchangeOneDayResponse")
	@GetMapping("/exchange-one-day")
	public ResponseEntity<PageResponse> getExchangeOneDayPage(
		@ParameterObject @ModelAttribute PaymentExchangeOneDaySearchRequest request) {
		return paymentService.getExchangeOneDayPage(request);
	}
}

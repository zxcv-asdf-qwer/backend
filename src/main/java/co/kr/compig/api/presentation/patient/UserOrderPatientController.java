package co.kr.compig.api.presentation.patient;

import java.util.Map;

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

import co.kr.compig.api.application.patient.OrderPatientService;
import co.kr.compig.api.presentation.patient.request.OrderPatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.OrderPatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.OrderPatientDetailResponse;
import co.kr.compig.global.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/order-patient", produces = "application/json")
public class UserOrderPatientController {

	private final OrderPatientService orderPatientService;

	@PostMapping
	public ResponseEntity<Response<?>> createOrderPatient(
		@ModelAttribute @Valid OrderPatientCreateRequest orderPatientCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("orderPatientId", orderPatientService.createOrderPatientUser(orderPatientCreateRequest)))
			.build());
	}

	@GetMapping(path = "/{orderPatientId}")
	public ResponseEntity<Response<OrderPatientDetailResponse>> getOrderPatient(
		@PathVariable(name = "orderPatientId") Long orderPatientId
	) {
		return ResponseEntity.ok(Response.<OrderPatientDetailResponse>builder()
			.data(orderPatientService.getOrderPatient(orderPatientId))
			.build());
	}

	@PutMapping(path = "/{orderPatientId}")
	public ResponseEntity<Response<?>> updateOrderPatient(
		@PathVariable(name = "orderPatientId") Long orderPatientId,
		@RequestBody @Valid OrderPatientUpdateRequest orderPatientUpdateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("orderPatientId",
				orderPatientService.updateOrderPatient(orderPatientId, orderPatientUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{orderPatientId}")
	public ResponseEntity<Response<?>> deleteOrderPatient(
		@PathVariable(name = "orderPatientId") Long orderPatientId
	) {
		orderPatientService.deleteOrderPatient(orderPatientId);
		return ResponseEntity.ok().build();
	}
}

package co.kr.compig.api.presentation.patient;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
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
import co.kr.compig.api.presentation.patient.request.AdminOrderPatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.OrderPatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.OrderPatientDetailResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 간병 공고 후 환자 정보", description = "간병 공고 후 환자 정보 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/order-patient", produces = "application/json")
public class AdminOrderPatientController {

	private final OrderPatientService orderPatientService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createOrderPatient(
		@ParameterObject @ModelAttribute @Valid AdminOrderPatientCreateRequest adminOrderPatientCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("orderPatientId", orderPatientService.createOrderPatientAdmin(adminOrderPatientCreateRequest)))
			.build());
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{orderPatientId}")
	public ResponseEntity<Response<OrderPatientDetailResponse>> getOrderPatient(
		@PathVariable(name = "orderPatientId") Long orderPatientId) {
		return ResponseEntity.ok(Response.<OrderPatientDetailResponse>builder()
			.data(orderPatientService.getOrderPatient(orderPatientId))
			.build());
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping(path = "/{orderPatientId}")
	public ResponseEntity<Response<?>> updateOrderPatient(
		@PathVariable(name = "orderPatientId") Long orderPatientId,
		@RequestBody @Valid OrderPatientUpdateRequest orderPatientUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("orderPatientId",
				orderPatientService.updateOrderPatient(orderPatientId, orderPatientUpdateRequest)))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{orderPatientId}")
	public ResponseEntity<Response<?>> deleteOrderPatient(
		@PathVariable(name = "orderPatientId") Long orderPatientId) {
		orderPatientService.deleteOrderPatient(orderPatientId);
		return ResponseEntity.ok().build();
	}
}

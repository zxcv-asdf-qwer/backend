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

import co.kr.compig.api.application.patient.PatientService;
import co.kr.compig.api.presentation.patient.request.AdminPatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.PatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.PatientDetailResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 환자 정보", description = "환자 정보 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/patient", produces = "application/json")
public class AdminPatientController {
	private final PatientService patientService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createPatient(
		@ModelAttribute @Valid AdminPatientCreateRequest adminPatientCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("patientId", patientService.createPatientAdmin(adminPatientCreateRequest)))
			.build());
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{patientId}")
	public ResponseEntity<Response<PatientDetailResponse>> getPatient(
		@PathVariable(name = "patientId") Long patientId
	) {
		return ResponseEntity.ok(Response.<PatientDetailResponse>builder()
			.data(patientService.getPatient(patientId))
			.build());
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping(path = "/{patientId}")
	public ResponseEntity<Response<?>> updatePatient(
		@PathVariable(name = "patientId") Long patientId,
		@RequestBody @Valid PatientUpdateRequest patientUpdateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("patientId", patientService.updatePatient(patientId, patientUpdateRequest)))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{patientId}")
	public ResponseEntity<Response<?>> deletePatient(
		@PathVariable(name = "patientId") Long patientId
	) {
		patientService.deletePatient(patientId);
		return ResponseEntity.ok().build();
	}
}

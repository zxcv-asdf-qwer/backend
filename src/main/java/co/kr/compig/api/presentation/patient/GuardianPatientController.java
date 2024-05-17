package co.kr.compig.api.presentation.patient;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.patient.PatientService;
import co.kr.compig.api.presentation.patient.request.PatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.PatientSearchRequest;
import co.kr.compig.api.presentation.patient.request.PatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.PatientDetailResponse;
import co.kr.compig.api.presentation.patient.response.PatientResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "간병인 환자 정보", description = "환자 정보 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/guardian/patient", produces = "application/json")
public class GuardianPatientController {
	private final PatientService patientService;

	@Operation(summary = "환자 프로필 등록")
	@PostMapping
	public ResponseEntity<Response<?>> createPatient(
		@RequestBody @Valid PatientCreateRequest patientCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("patientId", patientService.createPatientUser(patientCreateRequest)))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<SliceResponse<PatientResponse>> pageListPatient(
		@ParameterObject @ModelAttribute @Valid PatientSearchRequest patientSearchRequest, Pageable pageable) {
		Slice<PatientResponse> slice = patientService.pageListPatientCursor(patientSearchRequest, pageable);
		SliceResponse<PatientResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable,
			slice.hasNext(),
			slice.getContent() != null ? slice.getContent().get(slice.getContent().size() - 1).getId().toString() : "");
		return ResponseEntity.ok(sliceResponse);
	}

	@Operation(summary = "환자 상세 조회")
	@GetMapping(path = "/{patientId}")
	public ResponseEntity<Response<PatientDetailResponse>> getPatient(
		@PathVariable(name = "patientId") Long patientId) {
		return ResponseEntity.ok(Response.<PatientDetailResponse>builder()
			.data(patientService.getPatient(patientId))
			.build());
	}

	@Operation(summary = "환자 정보 수정")
	@PutMapping(path = "/{patientId}")
	public ResponseEntity<Response<?>> updateBoard(
		@PathVariable(name = "patientId") Long patientId,
		@RequestBody @Valid PatientUpdateRequest patientUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("patientId", patientService.updatePatient(patientId, patientUpdateRequest)))
			.build());
	}

	@Operation(summary = "환자 정보 삭제")
	@DeleteMapping(path = "/{patientId}")
	public ResponseEntity<Response<?>> deletePatient(
		@PathVariable(name = "patientId") Long patientId) {
		patientService.deletePatient(patientId);
		return ResponseEntity.ok().build();
	}

}

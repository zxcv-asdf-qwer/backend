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
import co.kr.compig.api.presentation.patient.request.PatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.PatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.PatientDetailResponse;
import co.kr.compig.global.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/patient", produces = "application/json")
public class UserPatientController {
	private final PatientService patientService;

	@PostMapping
	public ResponseEntity<Response<?>> createPatient(
		@ModelAttribute @Valid PatientCreateRequest patientCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("patientId", patientService.createPatientUser(patientCreateRequest)))
			.build());
	}

	@GetMapping(path = "/{patientId}")
	public ResponseEntity<Response<PatientDetailResponse>> getPatient(
		@PathVariable(name = "patientId") Long patientId
	) {
		return ResponseEntity.ok(Response.<PatientDetailResponse>builder()
			.data(patientService.getPatient(patientId))
			.build());
	}

	@PutMapping(path = "/{patientId}")
	public ResponseEntity<Response<?>> updateBoard(
		@PathVariable(name = "patientId") Long patientId,
		@RequestBody @Valid PatientUpdateRequest patientUpdateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("patientId", patientService.updatePatient(patientId, patientUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{patientId}")
	public ResponseEntity<Response<?>> deletePatient(
		@PathVariable(name = "patientId") Long patientId
	) {
		patientService.deletePatient(patientId);
		return ResponseEntity.ok().build();
	}

}

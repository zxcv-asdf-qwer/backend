package co.kr.compig.api.presentation.hospital;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import co.kr.compig.api.application.hospital.HospitalService;
import co.kr.compig.api.presentation.hospital.request.HospitalCreateRequest;
import co.kr.compig.api.presentation.hospital.request.HospitalSearchRequest;
import co.kr.compig.api.presentation.hospital.request.HospitalUpdateRequest;
import co.kr.compig.api.presentation.hospital.response.HospitalDetailResponse;
import co.kr.compig.api.presentation.hospital.response.HospitalResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/hospital", produces = "application/json")
public class AdminHospitalController {
	private final HospitalService hospitalService;

	@PostMapping
	public ResponseEntity<Response<?>> createHospital(
		@ModelAttribute @Valid HospitalCreateRequest hospitalCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("hospitalId", hospitalService.createHospital(hospitalCreateRequest)))
			.build());
	}

	@GetMapping
	public ResponseEntity<PageResponse<HospitalResponse>> pageListHospital(@ModelAttribute @Valid
	HospitalSearchRequest hospitalSearchRequest, Pageable pageable) {
		Page<HospitalResponse> page = hospitalService.pageListHospital(hospitalSearchRequest, pageable);
		PageResponse<HospitalResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@GetMapping(path = "/{hospitalId}")
	public ResponseEntity<Response<HospitalDetailResponse>> getHospital(
		@PathVariable(name = "hospitalId") Long hospitalId) {
		return ResponseEntity.ok(Response.<HospitalDetailResponse>builder()
			.data(hospitalService.getHospital(hospitalId))
			.build());
	}

	@PutMapping(path = "/{hospitalId}")
	public ResponseEntity<Response<?>> updateHospital(@PathVariable(name = "hospitalId") Long hospitalId,
		@RequestBody @Valid HospitalUpdateRequest hospitalUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("hospitalId", hospitalService.updateHospital(hospitalId, hospitalUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{hospitalId}")
	public ResponseEntity<Response<?>> deleteHospital(@PathVariable(name = "hospitalId") Long hospitalId) {
		hospitalService.deleteHospital(hospitalId);
		return ResponseEntity.ok().build();
	}
}
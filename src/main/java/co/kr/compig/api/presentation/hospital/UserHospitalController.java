package co.kr.compig.api.presentation.hospital;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.hospital.HospitalService;
import co.kr.compig.api.presentation.hospital.request.HospitalSearchRequest;
import co.kr.compig.api.presentation.hospital.response.HospitalDetailResponse;
import co.kr.compig.api.presentation.hospital.response.HospitalResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "유저 병원정보", description = "병원정보 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/hospital", produces = "application/json")
public class UserHospitalController {
	private final HospitalService hospitalService;

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<SliceResponse<HospitalResponse>> pageListHospital(
		@ParameterObject @RequestParam @Valid HospitalSearchRequest hospitalSearchRequest,
		@RequestParam Pageable pageable) {
		Slice<HospitalResponse> slice = hospitalService.pageListHospitalCursor(hospitalSearchRequest, pageable);
		SliceResponse<HospitalResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable,
			slice.hasNext());
		return ResponseEntity.ok(sliceResponse);
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{hospitalId}")
	public ResponseEntity<Response<HospitalDetailResponse>> getHospital(
		@PathVariable(name = "hospitalId") Long hospitalId) {
		return ResponseEntity.ok(Response.<HospitalDetailResponse>builder()
			.data(hospitalService.getHospital(hospitalId))
			.build());
	}
}

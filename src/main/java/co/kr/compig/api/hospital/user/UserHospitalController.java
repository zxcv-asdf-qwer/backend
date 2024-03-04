package co.kr.compig.api.hospital.user;

import co.kr.compig.api.hospital.dto.HospitalDetailResponse;
import co.kr.compig.api.hospital.dto.HospitalResponse;
import co.kr.compig.api.hospital.dto.HospitalSearchRequest;
import co.kr.compig.common.dto.Response;
import co.kr.compig.common.dto.pagination.SliceResponse;
import co.kr.compig.service.hospital.HospitalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/hospital", produces = "application/json")
public class UserHospitalController {
  private final HospitalService hospitalService;
  @GetMapping
  public ResponseEntity<SliceResponse<HospitalResponse>> pageListHospital(
      @RequestBody @Valid HospitalSearchRequest hospitalSearchRequest, Pageable pageable){
    Slice<HospitalResponse> slice = hospitalService.pageListHospitalCursor(hospitalSearchRequest, pageable);
    SliceResponse<HospitalResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable,
        slice.hasNext());
    return ResponseEntity.ok(sliceResponse);
  }
  @GetMapping("/{hospitalId}")
  public ResponseEntity<Response<HospitalDetailResponse>> getHospital(
      @PathVariable(name = "hospitalId") Long hospitalId){
    return ResponseEntity.ok(Response.<HospitalDetailResponse>builder()
        .data(hospitalService.getHospital(hospitalId))
        .build());
  }
}

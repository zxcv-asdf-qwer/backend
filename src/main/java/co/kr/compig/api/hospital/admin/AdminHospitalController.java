package co.kr.compig.api.hospital.admin;

import co.kr.compig.api.hospital.dto.HospitalCreateRequest;
import co.kr.compig.api.hospital.dto.HospitalDetailResponse;
import co.kr.compig.api.hospital.dto.HospitalResponse;
import co.kr.compig.api.hospital.dto.HospitalSearchRequest;
import co.kr.compig.api.hospital.dto.HospitalUpdateRequest;
import co.kr.compig.common.dto.Response;
import co.kr.compig.common.dto.pagination.PageResponse;
import co.kr.compig.service.hospital.HospitalService;
import jakarta.validation.Valid;
import jakarta.xml.bind.JAXBException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/hospital", produces = "application/json")
public class AdminHospitalController {
  private final HospitalService hospitalService;

  @PostMapping
  public ResponseEntity<Response<?>> createHospital(
      @ModelAttribute @Valid HospitalCreateRequest hospitalCreateRequest){
    return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
        .data(Map.of("hospitalId", hospitalService.createHospital(hospitalCreateRequest)))
        .build());
  }

  @GetMapping
  public ResponseEntity<PageResponse<HospitalResponse>> pageListHospital(@RequestBody @Valid
      HospitalSearchRequest hospitalSearchRequest, Pageable pageable){
    Page<HospitalResponse> page = hospitalService.pageListHospital(hospitalSearchRequest, pageable);
    PageResponse<HospitalResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
        page.getTotalElements());
    return ResponseEntity.ok(pageResponse);
  }
  @GetMapping("/{hospitalId}")
  public ResponseEntity<Response<HospitalDetailResponse>> getHospital(
      @PathVariable(name = "hospitalId") Long hospitalId){
    return ResponseEntity.ok(Response.<HospitalDetailResponse>builder()
        .data(hospitalService.getHospital(hospitalId))
        .build());
  }

  @PutMapping("/{hospitalId}")
  public ResponseEntity<Response<?>> updateHospital(@PathVariable(name = "hospitalId") Long hospitalId,
      @RequestBody @Valid HospitalUpdateRequest hospitalUpdateRequest){
    return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
        .data(Map.of("hospitalId", hospitalService.updateHospital(hospitalId, hospitalUpdateRequest)))
        .build());
  }

  @DeleteMapping("/{hospitalId}")
  public ResponseEntity<Response<?>> deleteHospital(@PathVariable(name = "hospitalId") Long hospitalId){
    return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
        .data(Map.of("hospitalId", hospitalService.deleteHospital(hospitalId)))
        .build());
  }
}

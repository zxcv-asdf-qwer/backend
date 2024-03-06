package co.kr.compig.api.hospital.admin;

import co.kr.compig.api.hospital.dto.HospitalDetailResponse;
import co.kr.compig.api.hospital.dto.HospitalResponse;
import co.kr.compig.api.hospital.dto.HospitalSearchRequest;
import co.kr.compig.api.hospital.dto.HospitalUpdateRequest;
import co.kr.compig.common.dto.Response;
import co.kr.compig.common.dto.pagination.PageResponse;
import co.kr.compig.service.hospital.HospitalService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/hospital", produces = "application/json")
public class AdminHospitalController {
  private final HospitalService hospitalService;

  @GetMapping("/createAll")
  public ResponseEntity<Response<String>> createHospital(){
    return ResponseEntity.ok(Response.<String>builder()
        .data(hospitalService.createAllHospital())
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

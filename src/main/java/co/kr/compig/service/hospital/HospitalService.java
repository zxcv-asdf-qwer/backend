package co.kr.compig.service.hospital;

import co.kr.compig.api.hospital.dto.HospitalCreateRequest;
import co.kr.compig.api.hospital.dto.HospitalDetailResponse;
import co.kr.compig.api.hospital.dto.HospitalResponse;
import co.kr.compig.api.hospital.dto.HospitalSearchRequest;
import co.kr.compig.api.hospital.dto.HospitalUpdateRequest;
import co.kr.compig.common.exception.NotExistDataException;
import co.kr.compig.domain.hospital.Hospital;
import co.kr.compig.domain.hospital.HospitalRepository;
import co.kr.compig.domain.hospital.HospitalRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {
  private final HospitalRepository hospitalRepository;
  private final HospitalRepositoryCustom hospitalRepositoryCustom;

  public Long createHospital(HospitalCreateRequest hospitalCreateRequest) {
    Hospital hospital = hospitalCreateRequest.converterEntity();
    return hospitalRepository.save(hospital).getId();
  }

  @Transactional(readOnly = true)
  public Page<HospitalResponse> pageListHospital(HospitalSearchRequest hospitalSearchRequest, Pageable pageable){
    return hospitalRepositoryCustom.findPage(hospitalSearchRequest, pageable);
  }

  @Transactional(readOnly = true)
  public Slice<HospitalResponse> pageListHospitalCursor(HospitalSearchRequest hospitalSearchRequest, Pageable pageable){
    return hospitalRepositoryCustom.findAllByCondition(hospitalSearchRequest, pageable);
  }

  @Transactional(readOnly = true)
  public HospitalDetailResponse getHospital(Long hospitalId){
    Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(NotExistDataException::new);
    return hospital.toHospitalDetailResponse();
  }

  public Long updateHospital(Long hospitalId, HospitalUpdateRequest hospitalUpdateRequest){
    Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(NotExistDataException::new);
    hospital.update(hospitalUpdateRequest);
    return hospital.getId();
  }

  public Long deleteHospital(Long hospitalId) {
    Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(NotExistDataException::new);
    hospitalRepository.delete(hospital);
    return hospital.getId();
  }
}

package co.kr.compig.domain.hospital;

import co.kr.compig.api.hospital.dto.HospitalResponse;
import co.kr.compig.api.hospital.dto.HospitalSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepositoryCustom {

  Page<HospitalResponse> findPage(HospitalSearchRequest hospitalSearchRequest, Pageable pageable);
}

package co.kr.compig.api.domain.hospital;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.hospital.response.HospitalResponse;
import co.kr.compig.api.presentation.hospital.request.HospitalSearchRequest;

@Repository
public interface HospitalRepositoryCustom {

	Page<HospitalResponse> findPage(HospitalSearchRequest hospitalSearchRequest, Pageable pageable);

	Slice<HospitalResponse> findAllByCondition(HospitalSearchRequest hospitalSearchRequest, Pageable pageable);
}

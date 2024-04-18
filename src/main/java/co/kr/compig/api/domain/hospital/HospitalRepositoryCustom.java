package co.kr.compig.api.domain.hospital;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.hospital.request.HospitalSearchRequest;
import co.kr.compig.api.presentation.hospital.response.HospitalResponse;

@Repository
public interface HospitalRepositoryCustom {

	Page<HospitalResponse> findPage(HospitalSearchRequest hospitalSearchRequest);

	Slice<HospitalResponse> findAllByCondition(HospitalSearchRequest hospitalSearchRequest, Pageable pageable);
}

package co.kr.compig.api.application.hospital;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.hospital.Hospital;
import co.kr.compig.api.domain.hospital.HospitalRepository;
import co.kr.compig.api.domain.hospital.HospitalRepositoryCustom;
import co.kr.compig.api.presentation.hospital.request.HospitalCreateRequest;
import co.kr.compig.api.presentation.hospital.request.HospitalSearchRequest;
import co.kr.compig.api.presentation.hospital.request.HospitalUpdateRequest;
import co.kr.compig.api.presentation.hospital.response.HospitalDetailResponse;
import co.kr.compig.api.presentation.hospital.response.HospitalResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {

	private final HospitalRepository hospitalRepository;
	private final HospitalRepositoryCustom hospitalRepositoryCustom;

	@Transactional(readOnly = true)
	public Page<HospitalResponse> pageListHospital(HospitalSearchRequest hospitalSearchRequest,
		Pageable pageable) {
		return hospitalRepositoryCustom.findPage(hospitalSearchRequest, pageable);
	}

	@Transactional(readOnly = true)
	public Slice<HospitalResponse> pageListHospitalCursor(HospitalSearchRequest hospitalSearchRequest,
		Pageable pageable) {
		return hospitalRepositoryCustom.findAllByCondition(hospitalSearchRequest, pageable);
	}

	@Transactional(readOnly = true)
	public HospitalDetailResponse getHospital(Long hospitalId) {
		Hospital hospital = hospitalRepository.findById(hospitalId)
			.orElseThrow(NotExistDataException::new);
		return hospital.toHospitalDetailResponse();
	}

	public Long updateHospital(Long hospitalId, HospitalUpdateRequest hospitalUpdateRequest) {
		Hospital hospital = hospitalRepository.findById(hospitalId)
			.orElseThrow(NotExistDataException::new);
		hospital.update(hospitalUpdateRequest);
		return hospital.getId();
	}

	public void deleteHospital(Long hospitalId) {
		Hospital hospital = hospitalRepository.findById(hospitalId)
			.orElseThrow(NotExistDataException::new);
		hospitalRepository.delete(hospital);
	}

	public Long createHospital(HospitalCreateRequest hospitalCreateRequest) {
		Hospital hospital = hospitalCreateRequest.converterEntity();
		return hospitalRepository.save(hospital).getId();
	}

}


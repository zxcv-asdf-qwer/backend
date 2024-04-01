package co.kr.compig.api.domain.patient;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.patient.request.PatientSearchRequest;
import co.kr.compig.api.presentation.patient.response.PatientResponse;

@Repository
public interface PatientRepositoryCustom {
	Slice<PatientResponse> findAllByCondition(PatientSearchRequest patientSearchRequest, Pageable pageable);
}

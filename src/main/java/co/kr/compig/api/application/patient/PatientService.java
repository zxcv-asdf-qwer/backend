package co.kr.compig.api.application.patient;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.MemberRepository;
import co.kr.compig.api.domain.patient.Patient;
import co.kr.compig.api.domain.patient.PatientRepository;
import co.kr.compig.api.domain.patient.PatientRepositoryCustom;
import co.kr.compig.api.presentation.patient.request.AdminPatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.PatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.PatientSearchRequest;
import co.kr.compig.api.presentation.patient.request.PatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.PatientDetailResponse;
import co.kr.compig.api.presentation.patient.response.PatientResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

	private final PatientRepository patientRepository;
	private final PatientRepositoryCustom patientRepositoryCustom;
	private final MemberRepository memberRepository;

	public Long createPatientAdmin(AdminPatientCreateRequest patientCreateRequest) {
		Member member = memberRepository.findById(patientCreateRequest.getMemberId())
			.orElseThrow(NotExistDataException::new);
		Patient patient = patientCreateRequest.converterEntity(member);
		return patientRepository.save(patient).getId();
	}

	public Long createPatientUser(PatientCreateRequest patientCreateRequest) {
		Member member = memberRepository.findById(SecurityUtil.getMemberId())
			.orElseThrow(NotExistDataException::new);
		Patient patient = patientCreateRequest.converterEntity(member);
		return patientRepository.save(patient).getId();
	}

	@Transactional(readOnly = true)
	public PatientDetailResponse getPatient(Long patientId) {
		Patient patient = patientRepository.findById(patientId).orElseThrow(NoClassDefFoundError::new);
		return patient.toPatientDetailResponse();
	}

	public Long updatePatient(Long patientId, PatientUpdateRequest patientUpdateRequest) {
		Patient patient = patientRepository.findById(patientId).orElseThrow(NotExistDataException::new);
		patient.update(patientUpdateRequest);
		return patient.getId();
	}

	public void deletePatient(Long patientId) {
		Patient patient = patientRepository.findById(patientId).orElseThrow(NotExistDataException::new);
		patientRepository.delete(patient);
	}

	public Slice<PatientResponse> pageListPatientCursor(PatientSearchRequest patientSearchRequest, Pageable pageable) {
		return patientRepositoryCustom.findAllByCondition(patientSearchRequest, pageable);
	}
}

package co.kr.compig.api.application.patient;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.member.NoMemberService;
import co.kr.compig.api.domain.code.MemberType;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.NoMember;
import co.kr.compig.api.domain.patient.Patient;
import co.kr.compig.api.domain.patient.PatientRepository;
import co.kr.compig.api.domain.patient.PatientRepositoryCustom;
import co.kr.compig.api.presentation.patient.request.AdminPatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.PatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.PatientSearchRequest;
import co.kr.compig.api.presentation.patient.request.PatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.PatientDetailResponse;
import co.kr.compig.api.presentation.patient.response.PatientResponse;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

	private final MemberService memberService;
	private final NoMemberService noMemberService;

	private final PatientRepository patientRepository;
	private final PatientRepositoryCustom patientRepositoryCustom;

	public Long createPatientAdmin(AdminPatientCreateRequest adminPatientCreateRequest) {
		if (adminPatientCreateRequest.getMemberType().equals(MemberType.MEMBER)) {
			Member member = memberService.getMemberById(adminPatientCreateRequest.getMemberId());
			Patient patient = adminPatientCreateRequest.converterEntity(member);
			return patientRepository.save(patient).getId();
		} else {
			NoMember noMember = noMemberService.getNoMemberById(adminPatientCreateRequest.getMemberId());
			Patient patient = adminPatientCreateRequest.converterEntity(noMember);
			return patientRepository.save(patient).getId();
		}

	}

	public Long createPatientUser(PatientCreateRequest patientCreateRequest) {
		Member member = memberService.getMemberById(SecurityUtil.getMemberId());
		Patient patient = patientCreateRequest.converterEntity(member);
		return patientRepository.save(patient).getId();
	}

	@Transactional(readOnly = true)
	public PatientDetailResponse getPatient(Long patientId) {
		Patient patient = patientRepository.findById(patientId).orElseThrow(NotExistDataException::new);
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

	public List<PatientResponse> getPatients(String memberId, MemberType memberType) {
		if (memberId == null) {
			throw new BizException("memberId가 없습니다.");
		}
		if (memberType.equals(MemberType.MEMBER)) {
			Member member = memberService.getMemberById(memberId);
			return patientRepository.findAllByMember(member).stream()
				.map(Patient::toPatientResponse)
				.collect(Collectors.toList());
		} else {
			NoMember noMember = noMemberService.getNoMemberById(memberId);
			return patientRepository.findAllByNoMember(noMember).stream()
				.map(Patient::toPatientResponse)
				.collect(Collectors.toList());
		}
	}

	public Patient getOrderPatientByOrderPatientId(Long orderPatientId) {
		return patientRepository.findById(orderPatientId)
			.orElseThrow(NotExistDataException::new);
	}
}

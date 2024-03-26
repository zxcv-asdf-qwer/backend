package co.kr.compig.api.application.patient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.MemberRepository;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.patient.OrderPatientRepository;
import co.kr.compig.api.presentation.patient.request.OrderPatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.OrderPatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.OrderPatientDetailResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderPatientService {
	private final OrderPatientRepository orderPatientRepository;
	private final MemberRepository memberRepository;

	public Long createOrderPatient(OrderPatientCreateRequest orderPatientCreateRequest) {
		Member member = memberRepository.findById(orderPatientCreateRequest.getMemberId())
			.orElseThrow(NotExistDataException::new);
		OrderPatient orderPatient = orderPatientCreateRequest.converterEntity(member);
		return orderPatientRepository.save(orderPatient).getId();
	}

	public OrderPatientDetailResponse getOrderPatient(Long orderPatientId) {
		OrderPatient orderPatient = orderPatientRepository.findById(orderPatientId)
			.orElseThrow(NotExistDataException::new);
		return orderPatient.toOrderPatientDetailResponse();
	}

	public Long updateOrderPatient(Long orderPatientId, OrderPatientUpdateRequest orderPatientUpdateRequest) {
		OrderPatient orderPatient = orderPatientRepository.findById(orderPatientId)
			.orElseThrow(NotExistDataException::new);
		Member member = memberRepository.findById(orderPatientUpdateRequest.getMemberId())
			.orElseThrow(NotExistDataException::new);
		orderPatient.update(orderPatientUpdateRequest, member);
		return orderPatient.getId();
	}

	public void deleteOrderPatient(Long orderPatientId) {
		OrderPatient orderPatient = orderPatientRepository.findById(orderPatientId)
			.orElseThrow(NotExistDataException::new);
		orderPatientRepository.delete(orderPatient);
	}
}
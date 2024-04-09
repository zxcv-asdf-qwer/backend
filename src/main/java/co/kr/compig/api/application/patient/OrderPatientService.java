package co.kr.compig.api.application.patient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.patient.OrderPatientRepository;
import co.kr.compig.api.presentation.patient.request.AdminOrderPatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.OrderPatientCreateRequest;
import co.kr.compig.api.presentation.patient.request.OrderPatientUpdateRequest;
import co.kr.compig.api.presentation.patient.response.OrderPatientDetailResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderPatientService {
	private final MemberService memberService;
	private final OrderPatientRepository orderPatientRepository;

	public Long createOrderPatientAdmin(AdminOrderPatientCreateRequest adminOrderPatientCreateRequest) {
		Member member = memberService.getMemberById(adminOrderPatientCreateRequest.getMemberId());
		OrderPatient orderPatient = adminOrderPatientCreateRequest.converterEntity(member);
		return orderPatientRepository.save(orderPatient).getId();
	}

	public Long createOrderPatientUser(OrderPatientCreateRequest orderPatientCreateRequest) {
		Member member = memberService.getMemberById(SecurityUtil.getMemberId());
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
		orderPatient.update(orderPatientUpdateRequest);
		return orderPatient.getId();
	}

	public void deleteOrderPatient(Long orderPatientId) {
		OrderPatient orderPatient = orderPatientRepository.findById(orderPatientId)
			.orElseThrow(NotExistDataException::new);
		orderPatientRepository.delete(orderPatient);
	}

	public OrderPatient getOrderPatientByOrderPatientId(Long orderPatientId) {
		return orderPatientRepository.findById(orderPatientId)
			.orElseThrow(NotExistDataException::new);
	}
}

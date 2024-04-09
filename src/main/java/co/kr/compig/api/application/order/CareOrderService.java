package co.kr.compig.api.application.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.patient.OrderPatientService;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.MemberRepositoryCustom;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.order.CareOrderRepository;
import co.kr.compig.api.domain.order.CareOrderRepositoryCustom;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.presentation.order.request.AdminCareOrderCreateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderCreateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.request.CareOrderUpdateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.CareOrderResponse;
import co.kr.compig.global.dto.pagination.PageResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CareOrderService {

	private final MemberService memberService;
	private final MemberRepositoryCustom memberRepositoryCustom;
	private final OrderPatientService orderPatientService;
	private final CareOrderRepository careOrderRepository;
	private final CareOrderRepositoryCustom careOrderRepositoryCustom;

	public Long createCareOrderAdmin(AdminCareOrderCreateRequest adminCareOrderCreateRequest) {
		Member member = memberService.getMemberById(adminCareOrderCreateRequest.getMemberId());
		OrderPatient orderPatient = orderPatientService.getOrderPatientByOrderPatientId(
			adminCareOrderCreateRequest.getOrderPatientId());

		CareOrder careOrder = adminCareOrderCreateRequest.converterEntity(member, orderPatient);
		return careOrderRepository.save(careOrder).getId();
	}

	public Long createCareOrderUser(CareOrderCreateRequest careOrderCreateRequest) {
		Member member = memberService.getMemberById(careOrderCreateRequest.getMemberId());
		OrderPatient orderPatient = orderPatientService.getOrderPatientByOrderPatientId(
			careOrderCreateRequest.getOrderPatientId());

		CareOrder careOrder = careOrderCreateRequest.converterEntity(member, orderPatient);
		return careOrderRepository.save(careOrder).getId();
	}

	@Transactional(readOnly = true)
	public PageResponse<CareOrderResponse> pageListCareOrder(CareOrderSearchRequest careOrderSearchRequest,
		Pageable pageable) {

		Page<CareOrderResponse> page = careOrderRepositoryCustom.findPage(careOrderSearchRequest, pageable);
		return new PageResponse<>(page.getContent(), pageable, page.getTotalElements());
	}

	@Transactional(readOnly = true)
	public CareOrderDetailResponse getCareOrder(Long careOrderId) {
		CareOrder careOrder = careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
		return careOrder.toCareOrderDetailResponse();
	}

	public Long updateCareOrder(Long careOrderId, CareOrderUpdateRequest careOrderUpdateRequest) {
		CareOrder careOrder = careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
		careOrder.update(careOrderUpdateRequest);
		return careOrder.getId();
	}

	public void deleteCareOrder(Long careOrderId) {
		CareOrder careOrder = careOrderRepository.findById(careOrderId).orElseThrow(NotExistDataException::new);
		careOrderRepository.delete(careOrder);
	}

	public Slice<CareOrderResponse> pageListCareOrderCursor(CareOrderSearchRequest careOrderSearchRequest,
		Pageable pageable) {
		return careOrderRepositoryCustom.findAllByCondition(careOrderSearchRequest, pageable);
	}
}

package co.kr.compig.api.application.apply;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.domain.apply.Apply;
import co.kr.compig.api.domain.apply.ApplyRepository;
import co.kr.compig.api.domain.apply.ApplyRepositoryCustom;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.apply.request.ApplyCreateRequest;
import co.kr.compig.api.presentation.apply.request.ApplySearchRequest;
import co.kr.compig.api.presentation.apply.response.ApplyDetailResponse;
import co.kr.compig.api.presentation.apply.response.ApplyResponse;
import co.kr.compig.global.code.ApplyStatus;
import co.kr.compig.global.dto.pagination.SliceResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApplyService {

	private final ApplyRepository applyRepository;
	private final ApplyRepositoryCustom applyRepositoryCustom;
	private final CareOrderService careOrderService;
	private final MemberService memberService;

	public Long createApplyByAdmin(Long orderId, ApplyCreateRequest applyCreateRequest) {
		return applyRepository.findByCareOrder_idAndMember_id(orderId, applyCreateRequest.getMemberId())
			.map(Apply::getId)
			.orElseGet(() -> {
				Member member = memberService.getMemberById(applyCreateRequest.getMemberId());
				CareOrder careOrder = careOrderService.getCareOrderById(orderId);
				Apply apply = applyCreateRequest.converterEntity(member, careOrder);
				apply.setApplyStatus(ApplyStatus.MATCHING_WAIT);
				return applyRepository.save(apply).getId();
			});
	}

	public Long createApply(Long orderId, ApplyCreateRequest applyCreateRequest) {
		return applyRepository.findByCareOrder_idAndMember_id(orderId, SecurityUtil.getMemberId())
			.map(Apply::getId)
			.orElseGet(() -> {
				Member member = memberService.getMemberById(SecurityUtil.getMemberId());
				CareOrder careOrder = careOrderService.getCareOrderById(orderId);
				Apply apply = applyCreateRequest.converterEntity(member, careOrder);
				apply.setApplyStatus(ApplyStatus.MATCHING_WAIT);
				return applyRepository.save(apply).getId();
			});
	}

	@Transactional(readOnly = true)
	public List<ApplyDetailResponse> getApplies(Long orderId) {
		CareOrder careOrderById = careOrderService.getCareOrderById(orderId);
		return careOrderById.getApplys().stream()
			.map(Apply::toApplyDetailResponse)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ApplyDetailResponse getApply(Long orderId, String memberId) {
		Apply apply = applyRepository.findByCareOrder_idAndMember_id(orderId, memberId)
			.orElseThrow(NotExistDataException::new);
		return apply.toApplyDetailResponse();
	}

	public void deleteApply(Long orderId, String memberId) {
		Apply apply = applyRepository.findByCareOrder_idAndMember_id(orderId, memberId)
			.orElseThrow(NotExistDataException::new);
		CareOrder careOrder = careOrderService.getCareOrderById(apply.getCareOrder().getId());
		careOrder.removeApply(apply);
	}

	public SliceResponse<ApplyResponse> getApplySlice(Long orderId, ApplySearchRequest request) {
		Pageable pageable = request.pageable();
		Slice<ApplyResponse> slice = applyRepositoryCustom.getApplySlice(orderId, request, pageable);
		return new SliceResponse<>(slice.getContent(), pageable, slice.hasNext(),
			slice.getContent().getLast().getApplyId().toString());
	}

	public void updateMatchingComplete(Long orderId, String memberId) {
		Apply apply = applyRepository.findByCareOrder_idAndMember_id(orderId, memberId)
			.orElseThrow(NotExistDataException::new);
		//TODO 간병인이 간병중인지
		//TODO 간병인이 해야할 간병 중에 기간이 겹치는게 있는지
		apply.setApplyStatus(ApplyStatus.MATCHING_COMPLETE);
	}

	public void updateMatchingWait(Long orderId, String memberId) {
		Apply apply = applyRepository.findByCareOrder_idAndMember_id(orderId, memberId)
			.orElseThrow(NotExistDataException::new);
		apply.setApplyStatus(ApplyStatus.MATCHING_WAIT);
	}
}

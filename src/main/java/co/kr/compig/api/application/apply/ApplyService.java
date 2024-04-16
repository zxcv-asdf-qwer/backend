package co.kr.compig.api.application.apply;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.domain.apply.Apply;
import co.kr.compig.api.domain.apply.ApplyRepository;
import co.kr.compig.api.domain.apply.ApplyRepositoryCustom;
import co.kr.compig.api.domain.code.ApplyStatus;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.apply.request.ApplyCreateRequest;
import co.kr.compig.api.presentation.apply.request.ApplySearchRequest;
import co.kr.compig.api.presentation.apply.response.ApplyDetailResponse;
import co.kr.compig.api.presentation.apply.response.ApplyResponse;
import co.kr.compig.global.dto.pagination.PageResponse;
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
		Member member = memberService.getMemberById(applyCreateRequest.getMemberId());
		CareOrder careOrder = careOrderService.getCareOrderById(orderId);
		return applyRepository.findByMemberAndCareOrder(member, careOrder)
			.map(Apply::getId)
			.orElseGet(() -> {
				Apply apply = applyCreateRequest.converterEntity(member, careOrder);
				return applyRepository.save(apply).getId();
			});
	}

	public Long createApply(Long orderId, ApplyCreateRequest applyCreateRequest) {
		Member member = memberService.getMemberById(SecurityUtil.getMemberId());
		CareOrder careOrder = careOrderService.getCareOrderById(orderId);
		return applyRepository.findByMemberAndCareOrder(member, careOrder)
			.map(Apply::getId)
			.orElseGet(() -> {
				Apply apply = applyCreateRequest.converterEntity(member, careOrder);
				return applyRepository.save(apply).getId();
			});
	}

	@Transactional(readOnly = true)
	public List<ApplyResponse> getApplies(Long orderId, ApplySearchRequest searchRequest) {
		CareOrder careOrderById = careOrderService.getCareOrderById(orderId);
		return careOrderById.getApplys().stream()
			.map(Apply::toApplyResponse)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public PageResponse<ApplyResponse> getApplyPage(Long orderId, ApplySearchRequest searchRequest, Pageable pageable) {
		Page<ApplyResponse> page = applyRepositoryCustom.getApplyPage(orderId, searchRequest, pageable);
		return new PageResponse<>(page.getContent(), pageable, page.getTotalElements());
	}

	@Transactional(readOnly = true)
	public ApplyDetailResponse getApply(Long applyId) {
		Apply apply = applyRepository.findById(applyId).orElseThrow(NotExistDataException::new);
		Member member = memberService.getMemberById(apply.getMember().getId());
		CareOrder careOrder = careOrderService.getCareOrderById(apply.getCareOrder().getId());
		return apply.toApplyDetailResponse(member, careOrder);
	}

	public void deleteApply(Long applyId) {
		Apply apply = applyRepository.findById(applyId).orElseThrow(NotExistDataException::new);
		applyRepository.delete(apply);
	}

	public SliceResponse<ApplyResponse> getApplySlice(Long orderId, ApplySearchRequest applySearchRequest,
		Pageable pageable) {
		Slice<ApplyResponse> slice = applyRepositoryCustom.getApplySlice(orderId, applySearchRequest, pageable);
		return new SliceResponse<>(slice.getContent(), pageable, slice.hasNext());
	}

	public void updateMatchingComplete(Long applyId) {
		Apply apply = applyRepository.findById(applyId).orElseThrow(NotExistDataException::new);
		//TODO 간병인이 간병중인지
		//TODO 간병인이 해야할 간병 중에 기간이 겹치는게 있는지
		apply.setApplyStatus(ApplyStatus.MATCHING_COMPLETE);
	}

	public void updateMatchingWait(Long applyId) {
		Apply apply = applyRepository.findById(applyId).orElseThrow(NotExistDataException::new);
		apply.setApplyStatus(ApplyStatus.MATCHING_WAIT);
	}
}

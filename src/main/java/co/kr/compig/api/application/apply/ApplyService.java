package co.kr.compig.api.application.apply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.apply.Apply;
import co.kr.compig.api.domain.apply.ApplyRepository;
import co.kr.compig.api.domain.apply.ApplyRepositoryCustom;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.MemberRepository;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.order.CareOrderRepository;
import co.kr.compig.api.presentation.apply.request.ApplyCreateRequest;
import co.kr.compig.api.presentation.apply.request.ApplySearchRequest;
import co.kr.compig.api.presentation.apply.request.ApplyUpdateRequest;
import co.kr.compig.api.presentation.apply.response.ApplyDetailResponse;
import co.kr.compig.api.presentation.apply.response.ApplyResponse;
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
	private final CareOrderRepository careOrderRepository;
	private final MemberRepository memberRepository;

	public Long createApply(ApplyCreateRequest applyCreateRequest) {
		Member member = memberRepository.findById(SecurityUtil.getMemberId())
			.orElseThrow(NotExistDataException::new);
		CareOrder careOrder = careOrderRepository.findById(applyCreateRequest.getCareOrderId())
			.orElseThrow(NotExistDataException::new);
		Apply apply = applyCreateRequest.converterEntity(member, careOrder);
		return applyRepository.save(apply).getId();
	}

	public Page<ApplyResponse> pageListApply(Pageable pageable) {
		return applyRepositoryCustom.findPage(pageable);
	}

	public ApplyDetailResponse getApply(Long applyId) {
		Apply apply = applyRepository.findById(applyId).orElseThrow(NotExistDataException::new);
		Member member = memberRepository.findById(apply.getMember().getId()).orElseThrow(NotExistDataException::new);
		CareOrder careOrder = careOrderRepository.findById(apply.getCareOrder().getId())
			.orElseThrow(NotExistDataException::new);
		return apply.toApplyDetailResponse(member, careOrder);
	}

	public Long updateApply(Long applyId, ApplyUpdateRequest applyUpdateRequest) {
		Apply apply = applyRepository.findById(applyId).orElseThrow(NotExistDataException::new);
		apply.update(applyUpdateRequest);
		return apply.getId();
	}

	public void deleteApply(Long applyId) {
		Apply apply = applyRepository.findById(applyId).orElseThrow(NotExistDataException::new);
		applyRepository.delete(apply);
	}

	public Slice<ApplyResponse> pageListApplyCursor(ApplySearchRequest applySearchRequest, Pageable pageable) {
		return applyRepositoryCustom.findAllByCondition(applySearchRequest, pageable);
	}
}

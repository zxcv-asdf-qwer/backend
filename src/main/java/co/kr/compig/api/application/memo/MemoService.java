package co.kr.compig.api.application.memo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.domain.memo.Memo;
import co.kr.compig.api.domain.memo.MemoRepository;
import co.kr.compig.api.domain.memo.MemoRepositoryCustom;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.memo.request.AdminMemoCreateRequest;
import co.kr.compig.api.presentation.memo.response.MemoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemoService {

	private final MemoRepository memoRepository;
	private final MemoRepositoryCustom memoRepositoryCustom;
	private final CareOrderService careOrderService;
	private final MemberService memberService;

	public Long createMemo(Long orderId, AdminMemoCreateRequest adminMemoCreateRequest) {
		CareOrder careOrder = careOrderService.getCareOrderById(orderId);
		Memo memo = adminMemoCreateRequest.converterEntity(careOrder);
		memoRepository.save(memo);
		return memo.getId();
	}

	public List<MemoResponse> getMemoList(Long orderId) {
		List<MemoResponse> memoResponseList = memoRepositoryCustom.getMemoList(orderId);
		memoResponseList.forEach(memo -> {
			String userNm = memberService.getMemberById(memo.getCreatedBy()).getUserNm();
			memo.setCreatedBy(userNm);
		});

		return memoResponseList;
	}

}

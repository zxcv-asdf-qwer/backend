package co.kr.compig.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus implements BaseEnumCode<String> {
	MATCHING_WAITING("MATCHING_WAITING", "매칭대기"), //case1. 공고 등록 상태 / care2 공고 등록 + 간병인만 매칭 (미결제)
	MATCHING_COMPLETE("MATCHING_COMPLETE", "매칭완료"), //간병인 매칭 + 결제 완료 (결제 동기화 O)
	ORDER_CANCEL("ORDER_CANCEL", "매칭취소"), //case1. 매칭대기 상태에서 고객이 직접 취소한건 / care2. 어드민에서 취소 넘긴건 (결제랑 동기화 X )
	ORDER_COMPLETE("ORDER_COMPLETE", "간병완료"); //매칭 완료에서 => 간병이 다 끝난 경우

	private final String code;
	private final String desc;
}

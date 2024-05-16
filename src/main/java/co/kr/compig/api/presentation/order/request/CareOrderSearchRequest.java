package co.kr.compig.api.presentation.order.request;

import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.OrderStatus;
import co.kr.compig.global.code.OrderType;
import co.kr.compig.global.dto.pagination.PageableRequest;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class CareOrderSearchRequest extends PageableRequest {

	private String guardianMemberId; // 보호자 id
	private String partnerMemberId; // 간병인 id
	private IsYn publishYn; // 신규 여부
	@Parameter(description = "MATCHING_WAITING(매칭대기), MATCHING_COMPLETE(매칭완료), ORDER_CANCEL(매칭취소), ORDER_COMPLETE(간병완료)")
	private OrderStatus orderStatus; // 공고 상태
	private String userNm; // 보호자명
	private String telNo; // 전화번호
	@Parameter(description = "GENERAL(일반간병), FAMILY(가족간병)")
	private OrderType orderType; // 일반간병|가족간병
}

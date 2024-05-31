package co.kr.compig.api.presentation.order.response;

import java.time.LocalDateTime;

import co.kr.compig.global.code.OrderStatus;
import co.kr.compig.global.code.OrderType;
import co.kr.compig.global.code.PeriodType;
import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserCareOrderResponse extends BaseAudit {
	private Long id;
	private OrderStatus orderStatus; // 공고 상태
	private OrderType orderType;    // 가족 간병인지
	private Integer applyCount; // 지원자 수
	private String address1; // 주소
	private String address2; // 상세 주소
	private PeriodType periodType; // 시간제, 기간제
	private Integer amount; // 일,시급
	private LocalDateTime startDateTime; // 시작 일시
	private LocalDateTime endDateTime; // 종료 일시

}

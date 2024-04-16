package co.kr.compig.api.presentation.order.response;

import java.time.LocalDateTime;
import java.util.Set;

import co.kr.compig.api.domain.code.CareOrderProcessType;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.OrderStatus;
import co.kr.compig.api.presentation.apply.response.ApplyCareOrderResponse;
import co.kr.compig.api.presentation.patient.response.OrderPatientDetailResponse;
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
public class CareOrderDetailResponse {

	private Long orderId; // 공고 ID
	private LocalDateTime startDateTime; // 시작 날짜
	private LocalDateTime endDateTime; // 종료 날짜
	private OrderStatus orderStatus; // 공고 상태
	private IsYn publishYn; // 게시 여부
	private CareOrderProcessType careOrderProcessType; // 매칭 구분
	private String orderRequest; // 요청사항
	private String memberId; // userId
	private String userNm; // 보호자명
	private String telNo; // 보호자번호
	private OrderPatientDetailResponse orderPatient; //공고 기반 환자정보
	private Set<ApplyCareOrderResponse> applies; // 지원자

}

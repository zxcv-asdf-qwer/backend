package co.kr.compig.api.presentation.order.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import co.kr.compig.api.presentation.packing.response.FackingDetailResponse;
import co.kr.compig.api.presentation.patient.response.OrderPatientDetailResponse;
import co.kr.compig.global.code.CareOrderProcessType;
import co.kr.compig.global.code.IsYn;
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
public class CareOrderDetailResponse extends BaseAudit {

	private Long orderId; // 공고 ID
	private String memberId; // 보호자Id
	private String userNm; // 보호자명
	private String telNo; // 보호자번호
	private LocalDateTime startDateTime; // 시작 날짜
	private LocalDateTime endDateTime; // 종료 날짜
	private LocalDateTime realEndDateTime; // 진짜 종료 날짜
	private OrderStatus orderStatus; // 공고 상태
	private OrderType orderType; // 일반간병|가족간병
	private IsYn publishYn; // 게시 여부
	private CareOrderProcessType careOrderProcessType; // 매칭 구분
	private PeriodType periodType; // 시간제, 기간제
	private Integer amount; //금액 //보호자들이 입력한 금액, 수수료 계산전
	private Integer totalPrice; //총 금액
	private String orderRequest; // 요청사항

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private OrderPatientDetailResponse orderPatient; //공고 기반 환자정보
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private FackingDetailResponse fackingDetailResponse; //가족간병 정보
}

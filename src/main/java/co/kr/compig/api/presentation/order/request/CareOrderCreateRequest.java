package co.kr.compig.api.presentation.order.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.OrderType;
import co.kr.compig.global.code.PeriodType;
import co.kr.compig.global.validator.annotaion.Conditional;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Conditional(selected = "periodType", values = {"PART_TIME"}, required = {"partTime"})
public class CareOrderCreateRequest {

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH")
	private LocalDateTime startDateTime; // 시작 날짜

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH")
	private LocalDateTime endDateTime; // 종료 날짜

	private String title; // 제목

	private String orderRequest; // 요청 사항

	@NotNull
	@Builder.Default
	@Parameter(description = "게시 여부/작성자가 회원일 경우(=/admin/orders가 아니면) 보내지 않음")
	private IsYn publishYn = IsYn.Y; // 게시 여부 //작성자가 회원일 경우 무조건 게시

	@NotNull
	private PeriodType periodType;  // 시간제, 기간제

	@Parameter(description = "파트타임 시간 시간제 일 경우 필수")
	private Integer partTime; //파트타임 시간 시간제 일 경우 필수

	@NotNull
	private Integer amount; //금액 //보호자들이 입력한 금액, 수수료 계산전

	@NotNull
	private Long patientId; // 간병공고 등록시 환자 정보 ID

	public CareOrder converterEntity(Member member, OrderPatient orderPatient) {
		return CareOrder.builder()
			.startDateTime(this.startDateTime)
			.endDateTime(this.endDateTime)
			.title(this.title)
			.orderRequest(this.orderRequest)
			.publishYn(this.publishYn)
			.orderType(OrderType.GENERAL)
			.member(member)
			.orderPatient(orderPatient)
			.build();
	}

	public Packing toEntity(CareOrder careOrder, Settle settle, LocalDateTime startDateTime,
		LocalDateTime endDateTime) {
		return Packing.builder()
			.careOrder(careOrder)
			.settle(settle)
			.periodType(this.periodType)
			.amount(this.amount)
			.partTime(this.partTime)
			.startDateTime(startDateTime)
			.endDateTime(endDateTime)
			.build();
	}

}

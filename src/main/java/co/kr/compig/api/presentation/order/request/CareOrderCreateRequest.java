package co.kr.compig.api.presentation.order.request;

import java.time.LocalDateTime;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.OrderType;
import co.kr.compig.global.code.PeriodType;
import co.kr.compig.global.validator.annotaion.Conditional;
import io.swagger.v3.oas.annotations.media.Schema;
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
	@Schema(description = "시작 날짜", example = "2024-05-03 15:44:10")
	private LocalDateTime startDateTime; // 시작 날짜

	@NotNull
	@Schema(description = "시작 날짜", example = "2024-05-03 15:44:10")
	private LocalDateTime endDateTime; // 종료 날짜

	@Schema(description = "제목", example = "제목")
	private String title; // 제목

	@Schema(description = "요청 사항", example = "요청 사항")
	private String orderRequest; // 요청 사항

	@Builder.Default
	@Schema(description = "게시 여부/작성자가 회원일 경우(=/admin/orders가 아니면) 보내지 않음")
	private IsYn publishYn = IsYn.Y; // 게시 여부 //작성자가 회원일 경우 무조건 게시

	@NotNull
	@Schema(description = "시간제, 기간제", example = "PART_TIME")
	private PeriodType periodType;  // 시간제, 기간제

	@Schema(description = "파트타임 시간(시간제일 경우 필수)", example = "5")
	private Integer partTime; //파트타임 시간 시간제 일 경우 필수

	@NotNull
	@Schema(description = "금액", example = "180000")
	private Integer amount; //금액 //보호자들이 입력한 금액, 수수료 계산전

	@NotNull
	@Schema(description = "간병공고 등록 시 환자 정보 ID", example = "46")
	private Long patientId; // 간병공고 등록시 환자 정보 ID

	public CareOrder converterEntity(Member member, OrderPatient orderPatient) {
		return CareOrder.builder()
			.startDateTime(this.startDateTime)
			.endDateTime(this.endDateTime)
			.realEndDateTime(this.endDateTime)
			.title(this.title)
			.orderRequest(this.orderRequest)
			.publishYn(this.publishYn)
			.orderType(OrderType.GENERAL)
			.amount(this.amount)
			.periodType(this.periodType)
			.partTime(this.partTime)
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

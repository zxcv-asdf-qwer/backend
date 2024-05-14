package co.kr.compig.api.presentation.order.request;

import java.time.LocalDateTime;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.packing.Facking;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.global.code.OrderType;
import co.kr.compig.global.code.PeriodType;
import co.kr.compig.global.validator.annotaion.Conditional;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
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
public class FamilyCareOrderCreateRequest {

	@NotBlank
	private String partnerNm; // 간병인 이름

	@NotBlank
	private String partnerTelNo; // 간병인 전화번호

	@NotBlank
	private String addressCd; // 간병 장소 우편 번호

	@NotBlank
	private String address1; // 간병 장소 주소

	@NotBlank
	private String address2; // 간병 장소 상세 주소

	@NotNull
	private LocalDateTime startDateTime; // 시작 날짜

	@NotNull
	private LocalDateTime endDateTime; // 종료 날짜

	@NotBlank
	private String title; // 제목

	@NotBlank
	private String orderRequest; // 요청 사항

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
			.realEndDateTime(this.endDateTime)
			.title(this.title)
			.orderRequest(this.orderRequest)
			.orderType(OrderType.FAMILY)
			.member(member)
			.orderPatient(orderPatient)
			.build();
	}

	public Facking toEntity(CareOrder careOrder, Settle settle) {
		return Facking.builder()
			.careOrder(careOrder)
			.settle(settle)
			.partnerNm(this.partnerNm)
			.partnerTelNo(this.partnerTelNo)
			.addressCd(this.addressCd)
			.address1(this.address1)
			.address2(this.address2)
			.startDateTime(this.startDateTime)
			.endDateTime(this.endDateTime)
			.periodType(this.periodType)
			.partTime(this.partTime)
			.amount(this.amount)
			.build();
	}

	public CareOrderCalculateRequest toCareOrderCalculateRequest() {
		return CareOrderCalculateRequest.builder()
			.amount(this.amount)
			.startDateTime(this.startDateTime)
			.endDateTime(this.endDateTime)
			.periodType(this.periodType)
			.partTime(this.partTime)
			.build();
	}
}

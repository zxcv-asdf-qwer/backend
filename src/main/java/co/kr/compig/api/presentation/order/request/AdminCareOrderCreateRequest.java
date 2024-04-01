package co.kr.compig.api.presentation.order.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import co.kr.compig.api.domain.code.CareOrderRegisterType;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.PeriodType;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.patient.OrderPatient;
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
public class AdminCareOrderCreateRequest {

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startDateTime; // 시작 날짜

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endDateTime; // 종료 날짜

	private String orderRequest; // 요청 사항

	@NotNull
	private IsYn newStatus; // 신규 여부

	@NotNull
	private PeriodType periodType; // 시간제, 기간제

	@NotBlank
	private String memberId; // 멤버 ID

	@NotNull
	private Long orderPatientId; // 간병공고 등록시 환자 정보 ID

	public CareOrder converterEntity(Member member, OrderPatient orderPatient) {
		return CareOrder.builder()
			.startDateTime(this.startDateTime)
			.endDateTime(this.endDateTime)
			.orderRequest(this.orderRequest)
			.newStatus(this.newStatus)
			.periodType(this.periodType)
			.careOrderRegisterType(CareOrderRegisterType.MANUAL)
			.member(member)
			.orderPatient(orderPatient)
			.build();
	}
}
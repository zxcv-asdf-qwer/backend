package co.kr.compig.api.presentation.order.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.patient.OrderPatient;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.PeriodType;
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
public class CareOrderCreateRequest {

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH")
	private LocalDateTime startDateTime; // 시작 날짜

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH")
	private LocalDateTime endDateTime; // 종료 날짜

	@NotBlank
	private String title; // 제목

	private String orderRequest; // 요청 사항

	@NotNull
	private PeriodType periodType;  // 시간제, 기간제

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
			.publishYn(IsYn.Y)
			.member(member)
			.orderPatient(orderPatient)
			.build();
	}
}

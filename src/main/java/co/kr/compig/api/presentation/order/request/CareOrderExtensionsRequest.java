package co.kr.compig.api.presentation.order.request;

import java.time.LocalDateTime;

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
public class CareOrderExtensionsRequest {

	private LocalDateTime startDateTime; // 시작 날짜

	private LocalDateTime endDateTime; // 종료 날짜

	@NotNull
	private PeriodType periodType; // 시간제, 기간제

	@Parameter(description = "파트타임 시간 시간제 일 경우 필수")
	private Integer partTime; //파트타임 시간 시간제 일 경우 필수

	@NotNull
	private Integer amount; //금액 //보호자들이 입력한 금액, 수수료 계산전

}

package co.kr.compig.api.presentation.order.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import co.kr.compig.global.code.PeriodType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareOrderExtensionsRequest {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime startDateTime; // 시작 날짜

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime endDateTime; // 종료 날짜

	@NotNull
	private PeriodType periodType; // 시간제, 기간제

	@NotNull
	private Integer amount; //금액 //보호자들이 입력한 금액, 수수료 계산전

}

package co.kr.compig.api.presentation.order.request;

import java.time.LocalDateTime;

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
public class CareOrderTerminateRequest {

	@NotNull
	@Parameter(description = "강제 종료 날짜", example = "2024-05-03 15:44:10")
	private LocalDateTime realEndDateTime; // 강제 종료 날짜

}

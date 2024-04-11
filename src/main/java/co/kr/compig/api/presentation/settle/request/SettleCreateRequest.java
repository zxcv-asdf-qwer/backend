package co.kr.compig.api.presentation.settle.request;

import co.kr.compig.api.domain.settle.Settle;
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
public class SettleCreateRequest {
	@NotBlank
	private String element; // 요소명
	@NotNull
	private Integer amount; // 금액
	@NotNull
	private Long settleGroupId; // 간병요소 그룹 ID

	public Settle converterEntity() {
		return Settle.builder()
			.element(this.element)
			.amount(this.amount)
			.settleGroupId(this.settleGroupId)
			.build();
	}
}

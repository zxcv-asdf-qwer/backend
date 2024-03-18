package co.kr.compig.api.presentation.settle.request;

import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.domain.settle.SettleGroup;
import jakarta.validation.constraints.NotBlank;
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
	@NotBlank
	private Integer amount; // 금액

	public Settle converterEntity(SettleGroup settleGroup) {
		return Settle.builder()
			.element(this.element)
			.amount(this.amount)
			.settleGroup(settleGroup)
			.build();
	}
}

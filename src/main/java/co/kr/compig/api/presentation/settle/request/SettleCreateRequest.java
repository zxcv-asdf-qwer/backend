package co.kr.compig.api.presentation.settle.request;

import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.global.code.UseYn;
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
	@NotNull
	private Integer guardianFees; // 보호자 수수료
	@NotNull
	private Integer partnerFees; // 간병인 수수료
	@NotNull
	private UseYn useYn; // 사용 유무

	public Settle converterEntity() {
		return Settle.builder()
			.guardianFees(this.guardianFees)
			.partnerFees(this.partnerFees)
			.build();
	}
}

package co.kr.compig.api.presentation.settle.response;

import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SettleResponse extends BaseAudit {
	private Long settleId; // 간병금액정책 id
	private Integer guardianFees; // 보호자 수수료
	private Integer partnerFees; // 간병인 수수료

}

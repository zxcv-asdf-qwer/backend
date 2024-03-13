package co.kr.compig.api.presentation.settle.response;

import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.dto.BaseAudit;
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
	private String element; // 요소명
	private Integer amount; // 금액
	private Long settleGroupId; // 간병금액정책 그룹 id
	private UseYn useYn; // 사용 유무
}

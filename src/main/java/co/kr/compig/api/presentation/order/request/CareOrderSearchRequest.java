package co.kr.compig.api.presentation.order.request;

import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.PeriodType;
import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@SuperBuilder(toBuilder = true)
public class CareOrderSearchRequest extends PageableRequest {

	private IsYn publishYn; // 신규 여부
	private PeriodType periodType; // 시간제, 기간제
	private String userNm; // 보호자명
	private String telNo; // 전화번호
}

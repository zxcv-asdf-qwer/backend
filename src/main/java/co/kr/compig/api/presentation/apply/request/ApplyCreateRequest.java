package co.kr.compig.api.presentation.apply.request;

import co.kr.compig.api.domain.apply.Apply;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCreateRequest {

	@NotNull
	private Long careOrderId; // 공고 ID
	private String memberId; //회원 ID

	public Apply converterEntity(Member member, CareOrder careOrder) {
		return Apply.builder()
			.member(member)
			.careOrder(careOrder)
			.build();
	}
}

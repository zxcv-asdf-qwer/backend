package co.kr.compig.api.presentation.packing.request;

import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.settle.SettleGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackingCreateRequest {

	private Long careOrderId; // 공고 ID
	private Long settleGroupId; // settle ID

	public Packing converterEntity(CareOrder careOrder, SettleGroup settleGroup) {
		return Packing.builder()
			.careOrder(careOrder)
			.settleGroup(settleGroup)
			.build();
	}
}

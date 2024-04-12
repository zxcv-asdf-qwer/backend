package co.kr.compig.api.presentation.payment.request;

import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequest {
	private Long orderId; // 패킹 ID

	public Payment converterEntity(CareOrder careOrder) {
		return Payment.builder()
			.careOrder(careOrder)
			.build();
	}
}

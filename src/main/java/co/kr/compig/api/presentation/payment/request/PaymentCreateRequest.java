package co.kr.compig.api.presentation.payment.request;

import co.kr.compig.api.domain.packing.Packing;
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
	private Long packingId; // 패킹 ID

	public Payment converterEntity(Packing packing) {
		return Payment.builder()
			.packing(packing)
			.build();
	}
}

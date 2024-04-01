package co.kr.compig.api.presentation.payment.request;

import co.kr.compig.api.domain.payment.Payment;
import co.kr.compig.api.domain.payment.PaymentCancel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelCreateRequest {
	private Long paymentId; // 결제 ID

	public PaymentCancel converterEntity(Payment payment) {
		return PaymentCancel.builder()
			.payment(payment)
			.build();
	}
}

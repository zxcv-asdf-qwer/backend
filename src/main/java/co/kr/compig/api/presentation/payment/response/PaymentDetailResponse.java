package co.kr.compig.api.presentation.payment.response;

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
public class PaymentDetailResponse {
	private Long id; // 결제 ID
	private Long careOrderId; // 패킹 ID
}

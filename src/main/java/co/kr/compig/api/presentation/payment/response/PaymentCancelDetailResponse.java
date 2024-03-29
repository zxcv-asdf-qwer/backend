package co.kr.compig.api.presentation.payment.response;

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
public class PaymentCancelDetailResponse extends BaseAudit {
	private Long id; //paymentCancel ID
	private Long paymentId; //paymentId
}

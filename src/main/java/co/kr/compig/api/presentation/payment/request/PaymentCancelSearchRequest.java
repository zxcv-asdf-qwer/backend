package co.kr.compig.api.presentation.payment.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class PaymentCancelSearchRequest extends PageableRequest {
	private Long paymentId; // 결제 ID
}

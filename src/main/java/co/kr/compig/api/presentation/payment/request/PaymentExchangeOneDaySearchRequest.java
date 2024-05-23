package co.kr.compig.api.presentation.payment.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class PaymentExchangeOneDaySearchRequest extends PageableRequest {
	private Long careOrderId;
}

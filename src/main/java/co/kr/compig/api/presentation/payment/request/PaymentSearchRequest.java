package co.kr.compig.api.presentation.payment.request;

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
public class PaymentSearchRequest extends PageableRequest {
	private Long packingId;
}

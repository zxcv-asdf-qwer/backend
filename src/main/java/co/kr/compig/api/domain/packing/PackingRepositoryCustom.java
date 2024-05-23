package co.kr.compig.api.domain.packing;

import org.springframework.data.domain.Page;

import co.kr.compig.api.presentation.payment.request.PaymentExchangeOneDaySearchRequest;

public interface PackingRepositoryCustom {

	Page<Packing> getExchangeOneDayPage(PaymentExchangeOneDaySearchRequest request);

}

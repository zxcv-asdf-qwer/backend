package co.kr.compig.api.domain.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.payment.request.PaymentCancelSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentCancelResponse;

@Repository
public interface PaymentCancelRepositoryCustom {
	Page<PaymentCancelResponse> findPage(PaymentCancelSearchRequest paymentCancelSearchRequest);

	Slice<PaymentCancelResponse> getPaymentCancelSlice(PaymentCancelSearchRequest paymentCancelSearchRequest,
		Pageable pageable);
}

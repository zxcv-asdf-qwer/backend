package co.kr.compig.api.domain.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.payment.request.PaymentSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentResponse;

@Repository
public interface PaymentRepositoryCustom {
	Slice<PaymentResponse> getPaymentSlice(PaymentSearchRequest paymentSearchRequest, Pageable pageable);

	Page<PaymentResponse> findPage(PaymentSearchRequest paymentSearchRequest);
}

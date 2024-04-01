package co.kr.compig.api.application.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.payment.Payment;
import co.kr.compig.api.domain.payment.PaymentCancel;
import co.kr.compig.api.domain.payment.PaymentCancelRepository;
import co.kr.compig.api.domain.payment.PaymentCancelRepositoryCustom;
import co.kr.compig.api.domain.payment.PaymentRepository;
import co.kr.compig.api.presentation.payment.request.PaymentCancelCreateRequest;
import co.kr.compig.api.presentation.payment.request.PaymentCancelSearchRequest;
import co.kr.compig.api.presentation.payment.request.PaymentCancelUpdateRequest;
import co.kr.compig.api.presentation.payment.response.PaymentCancelDetailResponse;
import co.kr.compig.api.presentation.payment.response.PaymentCancelResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentCancelService {

	private final PaymentCancelRepository paymentCancelRepository;
	private final PaymentRepository paymentRepository;
	private final PaymentCancelRepositoryCustom paymentCancelRepositoryCustom;

	public Long createPaymentCancel(PaymentCancelCreateRequest paymentCancelCreateRequest) {
		Payment payment = paymentRepository.findById(paymentCancelCreateRequest.getPaymentId()).orElseThrow(
			NotExistDataException::new);
		PaymentCancel paymentCancel = paymentCancelCreateRequest.converterEntity(payment);
		return paymentCancelRepository.save(paymentCancel).getId();
	}

	public Page<PaymentCancelResponse> pageListPaymentCancel(PaymentCancelSearchRequest paymentCancelSearchRequest,
		Pageable pageable) {
		return paymentCancelRepositoryCustom.findPage(paymentCancelSearchRequest, pageable);
	}

	public PaymentCancelDetailResponse getPaymentCancel(Long paymentCancelId) {
		PaymentCancel paymentCancel = paymentCancelRepository.findById(paymentCancelId)
			.orElseThrow(NotExistDataException::new);
		return paymentCancel.toPaymentCancelDetailResponse();
	}

	public Long updatePaymentCancel(Long paymentCancelId, PaymentCancelUpdateRequest paymentCancelUpdateRequest) {
		PaymentCancel paymentCancel = paymentCancelRepository.findById(paymentCancelId)
			.orElseThrow(NotExistDataException::new);
		paymentCancel.update(paymentCancelUpdateRequest);
		return paymentCancel.getId();
	}

	public void deletePaymentCancel(Long paymentCancelId) {
		PaymentCancel paymentCancel = paymentCancelRepository.findById(paymentCancelId)
			.orElseThrow(NotExistDataException::new);
		paymentCancelRepository.delete(paymentCancel);
	}

	public Slice<PaymentCancelResponse> pageListPaymentCancelCursor(
		PaymentCancelSearchRequest paymentCancelSearchRequest, Pageable pageable) {
		return paymentCancelRepositoryCustom.findAllByCondition(paymentCancelSearchRequest, pageable);
	}
}

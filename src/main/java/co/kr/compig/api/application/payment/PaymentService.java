package co.kr.compig.api.application.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.packing.PackingRepository;
import co.kr.compig.api.domain.payment.Payment;
import co.kr.compig.api.domain.payment.PaymentRepository;
import co.kr.compig.api.domain.payment.PaymentRepositoryCustom;
import co.kr.compig.api.presentation.payment.request.PaymentCreateRequest;
import co.kr.compig.api.presentation.payment.request.PaymentSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentDetailResponse;
import co.kr.compig.api.presentation.payment.response.PaymentResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentRepositoryCustom paymentRepositoryCustom;
	private final PackingRepository packingRepository;

	public Long createPayment(PaymentCreateRequest paymentCreateRequest) {
		Packing packing = packingRepository.findById(paymentCreateRequest.getPackingId()).orElseThrow(
			NotExistDataException::new);
		Payment payment = paymentCreateRequest.converterEntity(packing);
		return paymentRepository.save(payment).getId();
	}

	@Transactional(readOnly = true)
	public PaymentDetailResponse getPayment(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId).orElseThrow(NotExistDataException::new);
		return payment.toPaymentDetailResponse();
	}

	public void deletePayment(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId).orElseThrow(NotExistDataException::new);
		paymentRepository.delete(payment);
	}

	public Slice<PaymentResponse> pageListPaymentCursor(PaymentSearchRequest paymentSearchRequest, Pageable pageable) {
		return paymentRepositoryCustom.findAllByCondition(paymentSearchRequest, pageable);
	}

	public Page<PaymentResponse> pageListPayment(PaymentSearchRequest paymentSearchRequest, Pageable pageable) {
		return paymentRepositoryCustom.findPage(paymentSearchRequest, pageable);
	}
}

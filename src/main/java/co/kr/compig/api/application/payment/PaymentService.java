package co.kr.compig.api.application.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.payment.Payment;
import co.kr.compig.api.domain.payment.PaymentRepository;
import co.kr.compig.api.domain.payment.PaymentRepositoryCustom;
import co.kr.compig.api.presentation.payment.request.PaymentCreateRequest;
import co.kr.compig.api.presentation.payment.request.PaymentSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentDetailResponse;
import co.kr.compig.api.presentation.payment.response.PaymentResponse;
import co.kr.compig.global.dto.pagination.PageResponse;
import co.kr.compig.global.dto.pagination.SliceResponse;
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
	private final CareOrderService careOrderService;

	public Long createPayment(PaymentCreateRequest paymentCreateRequest) {
		CareOrder careOrderById = careOrderService.getCareOrderById(paymentCreateRequest.getOrderId());
		Payment payment = paymentCreateRequest.converterEntity(careOrderById);
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

	@Transactional(readOnly = true)
	public SliceResponse<PaymentResponse> getPaymentSlice(PaymentSearchRequest paymentSearchRequest,
		Pageable pageable) {
		Slice<PaymentResponse> slice = paymentRepositoryCustom.getPaymentSlice(paymentSearchRequest, pageable);
		return new SliceResponse<>(slice.getContent(), pageable, slice.hasNext());
	}

	@Transactional(readOnly = true)
	public PageResponse<PaymentResponse> getPaymentPage(PaymentSearchRequest paymentSearchRequest, Pageable pageable) {
		Page<PaymentResponse> page = paymentRepositoryCustom.findPage(paymentSearchRequest, pageable);
		return new PageResponse<>(page.getContent(), pageable, page.getTotalElements());
	}

	@Transactional(readOnly = true)
	public Payment getPaymentById(Long paymentId) {
		return paymentRepository.findById(paymentId).orElseThrow(NotExistDataException::new);
	}
}

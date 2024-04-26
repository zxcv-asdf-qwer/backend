package co.kr.compig.api.application.payment;

import static co.kr.compig.global.utils.CalculateUtil.*;
import static co.kr.compig.global.utils.KeyGen.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.application.settle.SettleService;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.payment.Payment;
import co.kr.compig.api.domain.payment.PaymentRepository;
import co.kr.compig.api.domain.payment.PaymentRepositoryCustom;
import co.kr.compig.api.infrastructure.pay.PayApi;
import co.kr.compig.api.infrastructure.pay.model.SmsPayRequest;
import co.kr.compig.api.infrastructure.pay.model.SmsPayResponse;
import co.kr.compig.api.presentation.order.request.CareOrderCalculateRequest;
import co.kr.compig.api.presentation.payment.request.PaymentSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentDetailResponse;
import co.kr.compig.api.presentation.payment.response.PaymentResponse;
import co.kr.compig.global.dto.pagination.SliceResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.utils.GsonLocalDateTimeAdapter;
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
	private final SettleService settleService;
	private final PayApi payApi;

	@Value("${api.pay.mid}")
	private String payMid;

	public String createPayment(Long orderId) {
		CareOrder careOrder = careOrderService.getCareOrderById(orderId);
		AtomicInteger totalPrice = new AtomicInteger();

		Set<Packing> packages = careOrder.getPackages();
		packages.forEach(packing -> {
			CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
				.amount(packing.getAmount())
				.periodType(packing.getPeriodType())
				.partTime(packing.getPartTime())
				.build();
			totalPrice.addAndGet(calculatePaymentPriceOneDay(calculateRequest,
				packing.getSettle().getGuardianFees()));
		});

		SmsPayRequest smsPayRequest = SmsPayRequest.builder()
			.mid(payMid)
			.moid("CARE" + getRandomTimeKey())
			.goodsName(" " + packages.size() + "일") //상품명
			.amt(String.valueOf(totalPrice.get()))
			.buyerName(careOrder.getMember().getUserNm())
			.buyerTel(careOrder.getMember().getTelNo())
			.build();

		// 결제pg요청 프로세스
		ResponseEntity<String> response = payApi.requestSmsPay(smsPayRequest);
		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
			.create();

		SmsPayResponse smsPayResponse = gson.fromJson(
			Objects.requireNonNull(response.getBody()),
			SmsPayResponse.class
		);
		//return을 결제 url 로 넘기기
		careOrder.addPayment(smsPayResponse.toEntity(totalPrice.get()));
		paymentRepository.flush();
		return smsPayResponse.getOrderUrl();
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
	public Page<PaymentResponse> getPaymentPage(PaymentSearchRequest paymentSearchRequest) {
		return paymentRepositoryCustom.findPage(paymentSearchRequest);
	}

	@Transactional(readOnly = true)
	public Payment getPaymentById(Long paymentId) {
		return paymentRepository.findById(paymentId).orElseThrow(NotExistDataException::new);
	}
}

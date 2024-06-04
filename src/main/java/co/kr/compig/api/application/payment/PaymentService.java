package co.kr.compig.api.application.payment;

import static co.kr.compig.global.utils.CalculateUtil.*;
import static co.kr.compig.global.utils.KeyGen.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flywaydb.core.internal.util.CollectionsUtils;
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
import co.kr.compig.api.application.system.EncryptKeyService;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.packing.PackingRepository;
import co.kr.compig.api.domain.payment.Payment;
import co.kr.compig.api.domain.payment.PaymentRepository;
import co.kr.compig.api.domain.payment.PaymentRepositoryCustom;
import co.kr.compig.api.infrastructure.pay.PayApi;
import co.kr.compig.api.infrastructure.pay.model.SmsPayRequest;
import co.kr.compig.api.infrastructure.pay.model.SmsPayResponse;
import co.kr.compig.api.presentation.order.request.CareOrderCalculateRequest;
import co.kr.compig.api.presentation.payment.request.PaymentExchangeOneDaySearchRequest;
import co.kr.compig.api.presentation.payment.request.PaymentSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentDetailResponse;
import co.kr.compig.api.presentation.payment.response.PaymentExchangeOneDayResponse;
import co.kr.compig.api.presentation.payment.response.PaymentResponse;
import co.kr.compig.global.crypt.AES256;
import co.kr.compig.global.dto.pagination.PageResponse;
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
	private final PackingRepository packingRepository;
	private final CareOrderService careOrderService;
	private final PayApi payApi;
	private final EncryptKeyService encryptKeyService;

	@Value("${api.pay.mid}")
	private String payMid;

	public String createPayment(Long orderId) {
		CareOrder careOrder = careOrderService.getCareOrderById(orderId);
		careOrder.createOrderPacking();
		//간병비 계산
		AtomicInteger totalPrice = new AtomicInteger();

		Set<Packing> packages = careOrder.getPackages();
		packages.forEach(packing -> {
			CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
				.amount(careOrder.getAmount())
				.periodType(careOrder.getPeriodType())
				.partTime(careOrder.getPartTime())
				.build();
			totalPrice.addAndGet(calculatePaymentPriceOneDay(calculateRequest,
				careOrder.getSettle().getGuardianFees()));
		});

		// 결제pg요청 프로세스
		SmsPayRequest smsPayRequest = SmsPayRequest.builder()
			.mid(payMid)
			.moid("CARE" + getRandomTimeKey())
			.goodsName(" " + packages.size() + "일") //상품명
			.amt(String.valueOf(totalPrice.get()))
			.buyerName(careOrder.getMember().getUserNm())
			.buyerTel(careOrder.getMember().getTelNo())
			.build();
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

	@Transactional(readOnly = true)
	public SliceResponse<PaymentResponse> getPaymentSlice(PaymentSearchRequest paymentSearchRequest,
		Pageable pageable) {
		Slice<PaymentResponse> slice = paymentRepositoryCustom.getPaymentSlice(paymentSearchRequest, pageable);
		return new SliceResponse<>(slice.getContent(), pageable, slice.hasNext(),
			CollectionsUtils.hasItems(slice.getContent()) ?
				slice.getContent().get(slice.getContent().size() - 1).getId().toString() : "");
	}

	@Transactional(readOnly = true)
	public List<PaymentDetailResponse> getPaymentsByOrderId(Long orderId) {
		List<Payment> paymentOptional = paymentRepository.findByCareOrderId(orderId);
		return paymentOptional.stream().map(Payment::toPaymentDetailResponse).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ResponseEntity<PageResponse> getExchangeOneDayPage(PaymentExchangeOneDaySearchRequest request) {
		Page<Packing> page = packingRepository.getExchangeOneDayPage(request);
		if (CollectionUtils.isEmpty(page.getContent())) {
			return PageResponse.noResult();
		}
		AES256 aes256 = encryptKeyService.getEncryptKey();

		List<PaymentExchangeOneDayResponse> responses =
			page.get().map(packing -> {
				CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
					.amount(packing.getAmount())
					.periodType(packing.getPeriodType())
					.partTime(packing.getPartTime())
					.build();
				String accountName;
				String accountNumber;

				try {
					accountName = aes256.decrypt(packing.getCareOrder().getMember().getAccount().getAccountName(),
						packing.getCareOrder().getMember().getAccount().getIv());
				} catch (Exception e) {
					accountName = null;
					log.error("Decryption error for account name: {}", e.getMessage());
				}

				try {
					accountNumber = aes256.decrypt(packing.getCareOrder().getMember().getAccount().getAccountNumber(),
						packing.getCareOrder().getMember().getAccount().getIv());
				} catch (Exception e) {
					accountNumber = null;
					log.error("Decryption error for account number: {}", e.getMessage());
				}
				return PaymentExchangeOneDayResponse.builder()
					.userNm(packing.getCareOrder().getMember().getUserNm())
					.price(calculatePriceOneDay(calculateRequest))
					.partnerFee(packing.getSettle().getPartnerFees())
					.transactionAmount(
						calculatePriceOneDay(calculateRequest) - packing.getSettle().getPartnerFees())//간병하루 - 간병인 수수료
					.orderId(packing.getCareOrder().getId())
					.accountName(accountName)
					.bankName(packing.getCareOrder().getMember().getAccount() != null ?
						packing.getCareOrder().getMember().getAccount().getBankName() : null)
					.accountNumber(accountNumber)
					.build();
			}).collect(Collectors.toList());
		return PageResponse.ok(responses.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}
}

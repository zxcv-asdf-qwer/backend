package co.kr.compig.api.domain.payment;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.payment.response.PaymentDetailResponse;
import co.kr.compig.global.code.PaymentType;
import co.kr.compig.global.code.converter.PaymentTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payment1")
@SequenceGenerator(
	name = "payment_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "payment_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq_gen")
	@Column(name = "payment_id")
	private Long id;
	@Column(length = 15)
	@Convert(converter = PaymentTypeConverter.class)
	private PaymentType paymentType;
	@Column(length = 250)
	private String moid; // 상점주문번호(고유값)
	@Column(length = 250)
	private String goodsName; //상품명
	@Column(length = 50, nullable = false)
	private String amt; //결제요청금액
	@Column(length = 50)
	private String buyerName; //결제자 이름
	@Column(length = 50)
	private String buyerTel; //결제자 휴대폰번호
	@Column(length = 250)
	private String buyerEmail; //결제자 이메일 주소(선택)
	@Column
	private LocalDateTime payExpDate; //SMS 카드 결제 or 가상계좌 마감기한
	@Column(length = 250)
	private String smsCardOrderUrl; //SMS 카드 결제 URL
	@Column(length = 250)
	private String vbankTid; //거래고유번호
	@Column(length = 250)
	private String vbankAccountTel; //입금자 연락처
	@Column(length = 250)
	private String vbankBankCode; //가상계좌 은행코드
	@Column(length = 250)
	private String vbankNum; //가상계좌 번호
	@Column(length = 30)
	private String vbankAccountName; //가상계좌 입금자명
	@Column(length = 5)
	private String vbankCountryCode; //국적
	@Column(length = 250)
	private String vbankSocNo; //생년월일(여권번호)
	@Column(length = 250)
	private String payRequestResultCode; //결과코드
	@Column(length = 250)
	private String payRequestResultMsg; //결과메시지
	@Column
	private LocalDateTime payCompleteDate; //결제일
	@Column
	private String payNotiResultCode; // 입금결과코드(결제완료, 결제취소)
	@Column
	private String payNotiResultMsg; // 입금결과메시지
	@Column
	private String notiTransSeq; // 거래번호

	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_payment"))
	@ManyToOne(fetch = FetchType.LAZY)
	private CareOrder careOrder = new CareOrder();

	@Builder.Default
	@OneToMany(
		mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PaymentCancel> paymentCancels = new HashSet<>();

	/* =================================================================
 	 * Default columns
   	 ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	/* =================================================================
	* Relation method
	================================================================= */
	public void setCareOrder(CareOrder careOrder) {
		this.careOrder = careOrder;
	}

	public PaymentDetailResponse toPaymentDetailResponse() {
		PaymentDetailResponse build = PaymentDetailResponse.builder()
			.id(this.id)
			.careOrderId(this.careOrder.getId())
			.paymentExpireDate(this.payExpDate)
			.paymentCompleteDate(this.payCompleteDate)
			.paymentRequestResult(this.payRequestResultCode)
			.paymentResponseResult(this.payNotiResultCode)
			.paymentAmount(this.amt)
			.build();
		build.setCreatedAndUpdated(this.createdAndModified);
		return build;
	}
}

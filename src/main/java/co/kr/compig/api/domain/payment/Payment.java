package co.kr.compig.api.domain.payment;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.presentation.payment.response.PaymentDetailResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table
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

	@Column(length = 250)
	private String goodsName; // 상품명

	@Column(nullable = false)
	private Integer price; // 결제금액 amt

	@Column(length = 250)
	private String moid; //상점주문번호 (고유값)

	@Column(length = 250)
	private String orderUrl; // 결제 URL

	@Column(length = 250)
	private String buyerName; // 결제자 이름

	@Column(length = 250)
	private String buyerTel; // 결제자 휴대폰번호

	@Column(length = 250)
	private String buyerEmail; // 결제자 이메일 주소

	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime payExpDate; //SMS 결제 마감기한

	@Column
	private String payRequestResultCode; // 결과코드

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
	* Relation method
	================================================================= */
	public void setCareOrder(CareOrder careOrder) {
		this.careOrder = careOrder;
	}

	public PaymentDetailResponse toPaymentDetailResponse() {
		return PaymentDetailResponse.builder()
			.id(this.id)
			.careOrderId(this.careOrder.getId())
			.build();
	}
}

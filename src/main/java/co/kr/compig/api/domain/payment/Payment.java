package co.kr.compig.api.domain.payment;

import java.util.HashSet;
import java.util.Set;

import co.kr.compig.api.domain.packing.Packing;
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

	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@JoinColumn(name = "packing_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_payment"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Packing packing = new Packing();

	@Builder.Default
	@OneToMany(
		mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<PaymentCancel> paymentCancels = new HashSet<>();

	public PaymentDetailResponse toPaymentDetailResponse() {
		return PaymentDetailResponse.builder()
			.id(this.id)
			.packingId(this.packing.getId())
			.build();
	}
}

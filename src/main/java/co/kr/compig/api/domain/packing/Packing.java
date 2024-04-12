package co.kr.compig.api.domain.packing;

import java.util.HashSet;
import java.util.Set;

import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.payment.Payment;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.domain.wallet.Wallet;
import co.kr.compig.api.presentation.packing.request.PackingUpdateRequest;
import co.kr.compig.api.presentation.packing.response.PackingDetailResponse;
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
	name = "packing_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "packing_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Packing {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "packing_seq_gen")
	@Column(name = "packing_id")
	private Long id;

	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@JoinColumn(name = "care_order_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_packing"))
	@ManyToOne(fetch = FetchType.LAZY)
	private CareOrder careOrder = new CareOrder();

	@Builder.Default
	@JoinColumn(name = "settle_id", nullable = false, foreignKey = @ForeignKey(name = "fk03_packing"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Settle settle = new Settle();

	@Builder.Default
	@OneToMany(
		mappedBy = "packing", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Payment> payments = new HashSet<>();

	@Builder.Default
	@OneToMany(
		mappedBy = "packing", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Wallet> wallets = new HashSet<>();

	public PackingDetailResponse toPackingDetailResponse() {
		return PackingDetailResponse.builder()
			.packingId(this.id)
			.careOrderId(this.careOrder.getId())
			.build();
	}

	public void update(PackingUpdateRequest packingUpdateRequest) {
	}
}

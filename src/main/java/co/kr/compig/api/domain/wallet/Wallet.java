package co.kr.compig.api.domain.wallet;

import com.fasterxml.jackson.annotation.JsonBackReference;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.presentation.wallet.response.WalletDetailResponse;
import co.kr.compig.global.code.ExchangeType;
import co.kr.compig.global.code.TransactionType;
import co.kr.compig.global.code.converter.ExchangeTypeConverter;
import co.kr.compig.global.code.converter.TransactionTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
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
	name = "wallet_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "wallet_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Wallet {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_seq_gen")
	@Column(name = "wallet_id")
	private Long id;

	@Column(length = 15)
	@Convert(converter = TransactionTypeConverter.class)
	private TransactionType transactionType; //입금, 출금

	@Column(length = 15)
	@Convert(converter = ExchangeTypeConverter.class)
	private ExchangeType exchangeType; //수기, 자동

	@Column
	private Integer transactionAmount; //거래 금액

	@Column
	private Integer balance; //잔액

	@Column
	private String description; //사유

	/* =================================================================
	* Domain mapping
	================================================================= */

	@Builder.Default
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_wallet"))
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference//연관관계의 주인 Entity 에 선언, 직렬화가 되지 않도록 수행
	private Member member = new Member(); // Member id

	@Builder.Default
	@JoinColumn(name = "packing_id", foreignKey = @ForeignKey(name = "fk02_wallet"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Packing packing = new Packing();

	/* =================================================================
	* Default columns
	================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	/* =================================================================
	* Relation method
	================================================================= */

	/* =================================================================
 	 * Business
       ================================================================= */

	public WalletDetailResponse toWalletDetailResponse() {
		WalletDetailResponse build = WalletDetailResponse.builder()
			.walletId(this.id)
			.exchangeType(this.exchangeType)
			.transactionType(this.transactionType)
			.transactionAmount(this.transactionAmount)
			.balance(this.balance)
			.description(this.description)
			.orderId(this.packing != null ? this.packing.getCareOrder().getId() : null)
			.partnerFee(this.packing != null ? this.packing.getSettle().getPartnerFees() : null)
			.build();
		build.setCreatedAndUpdated(this.createdAndModified);

		return build;
	}

	public void update(Member member, Packing packing) {
		this.member = member;
		this.packing = packing;
	}
}

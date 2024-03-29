package co.kr.compig.api.domain.wallet;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.presentation.wallet.response.WalletDetailResponse;
import jakarta.persistence.Column;
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

	@Builder.Default
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_wallet"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member = new Member(); // Member id

	@Builder.Default
	@JoinColumn(name = "packing_id", nullable = false, foreignKey = @ForeignKey(name = "fk02_wallet"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Packing packing = new Packing();

	public WalletDetailResponse toWalletDetailResponse() {
		return WalletDetailResponse.builder()
			.memberId(this.member.getId())
			.packingId(this.packing.getId())
			.build();
	}

	public void update(Member member, Packing packing) {
		this.member = member;
		this.packing = packing;
	}
}

package co.kr.compig.api.domain.settle;

import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.api.presentation.settle.request.SettleUpdateRequest;
import co.kr.compig.api.presentation.settle.response.SettleResponse;
import co.kr.compig.global.embedded.Created;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	name = "settle_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "settle_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Settle {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settle_seq_gen")
	@Column(name = "settle_id")
	private Long id;

	@Column
	private Integer guardianFees; // 보호자 수수료 (%)

	@Column
	private Integer partnerFees; // 간병인 수수료 (원)

	@Column
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private UseYn useYn = UseYn.Y;
	/* =================================================================
	* Domain mapping
	================================================================= */

	/* =================================================================
	* Relation method
	================================================================= */
	public SettleResponse toSettleResponse() {
		return SettleResponse.builder()
			.settleId(this.id)
			.guardianFees(this.guardianFees)
			.partnerFees(this.partnerFees)
			.useYn(this.useYn)
			.build();
	}

	/* =================================================================
	* Default columns
	================================================================= */
	@Embedded
	@Builder.Default
	private Created created = new Created();

	public void update(SettleUpdateRequest settleUpdateRequest) {
		if (settleUpdateRequest.getGuardianFees() != null) {
			this.guardianFees = settleUpdateRequest.getGuardianFees();
		}

		if (settleUpdateRequest.getPartnerFees() != null) {
			this.partnerFees = settleUpdateRequest.getPartnerFees();
		}

		if (settleUpdateRequest.getUseYn() != null) {
			this.useYn = settleUpdateRequest.getUseYn();
		}
	}
}

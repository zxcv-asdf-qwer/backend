package co.kr.compig.api.presentation.wallet.request;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.wallet.Wallet;
import co.kr.compig.global.code.TransactionType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletCreateRequest {

	@Parameter(description = "간병인 ID")
	private String memberId; // 간병인 ID
	private int amount; //금액
	private TransactionType transactionType; //입금, 출금
	private String description; //사유

	public Wallet converterEntity(Member member) {
		return Wallet.builder()
			.member(member)
			.transactionType(this.transactionType)
			.description(this.description)
			.build();
	}
}

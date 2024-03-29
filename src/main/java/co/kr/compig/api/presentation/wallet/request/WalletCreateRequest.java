package co.kr.compig.api.presentation.wallet.request;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.wallet.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletCreateRequest {

	private String memberId; // 멤버 ID
	private Long packingId; // 패킹 ID

	public Wallet converterEntity(Member member, Packing packing) {
		return Wallet.builder()
			.member(member)
			.packing(packing)
			.build();
	}
}

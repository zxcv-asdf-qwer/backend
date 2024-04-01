package co.kr.compig.api.presentation.wallet.response;

import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class WalletResponse extends BaseAudit {

	private String memberId; // 멤버 ID
	private Long packingId; // 패킹 ID
}

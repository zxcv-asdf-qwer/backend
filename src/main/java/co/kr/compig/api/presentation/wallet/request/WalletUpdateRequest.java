package co.kr.compig.api.presentation.wallet.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalletUpdateRequest {
	private String memberId;
	private Long packingId;
}

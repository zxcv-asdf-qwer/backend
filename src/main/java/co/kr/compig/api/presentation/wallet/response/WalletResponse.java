package co.kr.compig.api.presentation.wallet.response;

import java.util.List;

import co.kr.compig.api.presentation.member.response.PartnerMemberResponse;
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

	private PartnerMemberResponse partnerMemberResponse;
	private List<WalletDetailResponse> walletDetailResponsesList; //목록 조회일때 가장 최신 만 조회
}

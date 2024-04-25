package co.kr.compig.api.presentation.wallet.response;

import co.kr.compig.global.code.ExchangeType;
import co.kr.compig.global.code.TransactionType;
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
public class WalletDetailResponse extends BaseAudit {

	private Long walletId; //ID
	private ExchangeType exchangeType; //수기, 자동
	private TransactionType transactionType; //입금, 출금
	private Integer transactionAmount; //거래금액
	private Integer balance; //잔액
	private String description; //사유
	private Long orderId; // 공고 ID
	private Integer partnerFee; // 간병인 수수료
}

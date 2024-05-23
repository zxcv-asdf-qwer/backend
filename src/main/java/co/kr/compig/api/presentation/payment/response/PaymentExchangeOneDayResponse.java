package co.kr.compig.api.presentation.payment.response;

import co.kr.compig.global.code.BankCode;
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
public class PaymentExchangeOneDayResponse extends BaseAudit {

	private String memberId; //간병인 ID
	private String userNm; //간병인 이름
	private Integer price; //간병비
	private Integer partnerFee; // 간병인 수수료
	private Integer transactionAmount; //지급 금액
	private Long orderId; // 공고 ID
	private String accountName; // 예금주
	private BankCode bankName; // 은행 이름
	private String accountNumber; // 계좌 번호
}

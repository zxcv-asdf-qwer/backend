package co.kr.compig.api.presentation.account.response;

import co.kr.compig.common.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AccountDetailResponse extends BaseAudit {

	private Long id; // 계좌 id
	private String accountNumber; // 계좌번호
	private String accountName; // 예금주
	private String bankName; // 은행 이름
}

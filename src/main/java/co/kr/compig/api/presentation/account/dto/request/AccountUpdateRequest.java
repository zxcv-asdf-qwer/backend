package co.kr.compig.api.presentation.account.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {

	private String accountNumber; // 계좌번호
	private String accountName; // 예금주
	private String bankName; // 은행 이름
}

package co.kr.compig.api.presentation.account.request;

import co.kr.compig.global.code.BankCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSaveRequest {

	@NotBlank
	private String accountNumber; // 계좌번호

	@NotBlank
	private String accountName; // 예금주

	@NotBlank
	private BankCode bankName; // 은행 이름

}

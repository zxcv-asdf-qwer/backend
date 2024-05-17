package co.kr.compig.api.presentation.account.request;

import java.util.Base64;

import co.kr.compig.api.domain.account.Account;
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

	public Account converterEntity(byte[] iv) {
		return Account.builder()
			.accountNumber(this.accountNumber)
			.accountName(this.accountName)
			.bankName(bankName)
			.iv(Base64.getUrlEncoder().encodeToString(iv))
			.build();
	}

}

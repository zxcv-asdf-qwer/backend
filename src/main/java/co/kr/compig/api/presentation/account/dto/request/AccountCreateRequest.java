package co.kr.compig.api.presentation.account.dto.request;

import java.util.Base64;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import co.kr.compig.common.code.BankCode;
import co.kr.compig.domain.account.Account;
import co.kr.compig.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequest {

	@NotBlank
	private String accountNumber; // 계좌번호

	@NotBlank
	private String accountName; // 예금주

	@NotBlank
	private String bankName; // 은행 이름

	@NotBlank
	private String memberId; // 멤버 id

	public Account converterEntity(Member member, byte[] iv) {
		return Account.builder()
			.accountNumber(this.accountNumber)
			.accountName(this.accountName)
			.bankName(BankCode.of(this.bankName))
			.member(member)
			.iv(Base64.getUrlEncoder().encodeToString(iv))
			.build();
	}

}

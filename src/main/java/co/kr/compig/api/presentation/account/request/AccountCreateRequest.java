package co.kr.compig.api.presentation.account.request;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import co.kr.compig.api.domain.account.Account;
import co.kr.compig.api.domain.code.BankCode;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.presentation.board.response.SystemFileResponse;
import jakarta.validation.constraints.NotBlank;
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

	private String passBookUrl; // 통장사본 url

	public Account converterEntity(Member member, byte[] iv) {
		return Account.builder()
			.accountNumber(this.accountNumber)
			.accountName(this.accountName)
			.bankName(BankCode.of(this.bankName))
			.member(member)
			.passBookUrl(passBookUrl)
			.iv(Base64.getUrlEncoder().encodeToString(iv))
			.build();
	}

	public void setPassBookUrl(List<SystemFileResponse> imageUrlList) {
		List<String> imageUrls = new ArrayList<>();
		for (SystemFileResponse systemFileResponse : imageUrlList) {
			imageUrls.add(systemFileResponse.getFilePath());
		}
		this.passBookUrl = imageUrlList.get(0).getFilePath();
	}
}

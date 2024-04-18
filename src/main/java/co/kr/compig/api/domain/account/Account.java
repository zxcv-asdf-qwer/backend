package co.kr.compig.api.domain.account;

import java.util.Base64;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.presentation.account.request.AccountUpdateRequest;
import co.kr.compig.api.presentation.account.response.AccountDetailResponse;
import co.kr.compig.global.code.BankCode;
import co.kr.compig.global.code.converter.BankCodeConverter;
import co.kr.compig.global.crypt.AES256;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@SequenceGenerator(
	name = "account_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "account_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq_gen")
	@Column(name = "account_id")
	private Long id;

	@Column(nullable = false)
	private String accountNumber; // 계좌 번호

	@Column(nullable = false)
	private String accountName; // 예금주

	@Column(nullable = false)
	@Convert(converter = BankCodeConverter.class)
	private BankCode bankName; // 은행 이름

	@Column(nullable = false)
	private String iv; // 이니셜 벡터

	@Column
	private String passBookUrl; // 통장사본
	/* =================================================================
	 * Domain mapping
	   ================================================================= */
	@Builder.Default
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_account"))
	@OneToOne(fetch = FetchType.LAZY)
	private Member member = new Member();

	public AccountDetailResponse toAccountDetailResponse() {
		return AccountDetailResponse.builder()
			.id(this.id)
			.accountNumber(this.accountNumber)
			.accountName(this.accountName)
			.bankName(this.bankName.getCode())
			.build();
	}

	public void update(AccountUpdateRequest accountUpdateRequest, AES256 aes256, byte[] iv)
		throws Exception {
		this.accountNumber = aes256.encrypt(accountUpdateRequest.getAccountNumber(), iv);
		this.accountName = aes256.encrypt(accountUpdateRequest.getAccountName(), iv);
		this.bankName = BankCode.of(accountUpdateRequest.getBankName());
		this.iv = Base64.getUrlEncoder().encodeToString(iv);
	}

	/* =================================================================
	* Default columns
	================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();
}

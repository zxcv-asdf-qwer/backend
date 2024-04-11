package co.kr.compig.api.application.account;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.domain.account.Account;
import co.kr.compig.api.domain.account.AccountRepository;
import co.kr.compig.api.domain.code.EncryptTarget;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.system.EncryptKey;
import co.kr.compig.api.domain.system.EncryptKeyRepository;
import co.kr.compig.api.presentation.account.request.AccountCreateRequest;
import co.kr.compig.api.presentation.account.request.AccountUpdateRequest;
import co.kr.compig.api.presentation.account.response.AccountDetailResponse;
import co.kr.compig.api.presentation.board.response.SystemFileResponse;
import co.kr.compig.global.crypt.AES256;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.global.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

	private final AccountRepository accountRepository;
	private final EncryptKeyRepository encryptKeyRepository;
	private final S3Util s3Util;
	private final MemberService memberService;

	public Long createAccount(AccountCreateRequest accountCreateRequest, Map<String, String> files) {
		EncryptKey encryptKey = encryptKeyRepository.findByEncryptTarget(EncryptTarget.ACCOUNT)
			.orElseThrow(NotExistDataException::new);
		AES256 aes256 = new AES256(encryptKey.getEncryptKey());
		byte[] iv = aes256.generateIv();
		try {
			accountCreateRequest.setAccountNumber(
				aes256.encrypt(accountCreateRequest.getAccountNumber(), iv));
			accountCreateRequest.setAccountName(
				aes256.encrypt(accountCreateRequest.getAccountName(), iv));
		} catch (Exception e) {
			throw new RuntimeException("AES256 암호화 중 예외발생");
		}

		Member member = memberService.getMemberById(accountCreateRequest.getMemberId());
		if (accountRepository.existsByMember(member)) {
			throw new RuntimeException("이미 계좌가 존재합니다.");
		}
		Account account = new Account();
		if (files != null) {
			List<SystemFileResponse> imageUrlList = s3Util.uploadBase64ToFile(files);
			accountCreateRequest.setPassBookUrl(imageUrlList);
		}
		account = accountCreateRequest.converterEntity(member, iv);
		accountRepository.save(account);
		return account.getId();
	}

	@Transactional(readOnly = true)
	public AccountDetailResponse getAccountByAccountId(Long accountId) {
		Account account = accountRepository.findById(accountId).orElseThrow(NotExistDataException::new);
		return accountToAccountDetailResponse(account);
	}

	@Transactional(readOnly = true)
	public AccountDetailResponse getAccountByMemberId(String memberId) {
		Member member = memberService.getMemberById(memberId);
		Account account = accountRepository.findByMember(member)
			.orElseThrow(NotExistDataException::new);
		return accountToAccountDetailResponse(account);
	}

	@Transactional(readOnly = true)
	public AccountDetailResponse accountToAccountDetailResponse(Account account) {
		EncryptKey encryptKey = encryptKeyRepository.findByEncryptTarget(EncryptTarget.ACCOUNT)
			.orElseThrow(NotExistDataException::new);
		AES256 aes256 = new AES256(encryptKey.getEncryptKey());
		try {
			return AccountDetailResponse.builder()
				.id(account.getId())
				.accountNumber(aes256.decrypt(account.getAccountNumber(), account.getIv()))
				.accountName(aes256.decrypt(account.getAccountName(), account.getIv()))
				.bankName(account.getBankName().getCode())
				.passBookUrl(account.getPassBookUrl())
				.build();
		} catch (Exception e) {
			throw new RuntimeException("AES256 복호화 중 예외발생");
		}
	}

	public Long updateAccount(Long accountId, AccountUpdateRequest accountUpdateRequest) {
		EncryptKey encryptKey = encryptKeyRepository.findByEncryptTarget(EncryptTarget.ACCOUNT)
			.orElseThrow(NotExistDataException::new);
		AES256 aes256 = new AES256(encryptKey.getEncryptKey());
		byte[] iv = aes256.generateIv();
		Account account = accountRepository.findById(accountId).orElseThrow(NotExistDataException::new);
		try {
			account.update(accountUpdateRequest, aes256, iv);
		} catch (Exception e) {
			throw new RuntimeException("AES256 암호화 중 예외발생");
		}
		return account.getId();
	}

	public void deleteAccount(Long accountId) {
		Account account = accountRepository.findById(accountId).orElseThrow(NotExistDataException::new);
		accountRepository.delete(account);
	}

	public Boolean getAccountCheck(String memberId) {
		Member member = memberService.getMemberById(memberId);
		return accountRepository.existsByMember(member);
	}
}

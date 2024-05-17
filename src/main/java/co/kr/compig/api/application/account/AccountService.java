package co.kr.compig.api.application.account;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.domain.account.Account;
import co.kr.compig.api.domain.account.AccountRepository;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.system.EncryptKey;
import co.kr.compig.api.domain.system.EncryptKeyRepository;
import co.kr.compig.api.presentation.account.request.AccountSaveRequest;
import co.kr.compig.api.presentation.account.response.AccountCheckResponse;
import co.kr.compig.api.presentation.account.response.AccountDetailResponse;
import co.kr.compig.global.code.EncryptTarget;
import co.kr.compig.global.crypt.AES256;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
	@Value("${api.account.url}")
	private String NICE_ACCOUNT_URL;
	@Value("${api.account.site-code}")
	private String NICE_ACCOUNT_SITE_CODE;
	@Value("${api.account.password}")
	private String NICE_ACCOUNT_PASSWORD;
	private final AccountRepository accountRepository;
	private final EncryptKeyRepository encryptKeyRepository;
	private final MemberService memberService;

	@Transactional(readOnly = true)
	public AccountDetailResponse getAccountByAccountId(Long accountId) {
		Account account = accountRepository.findById(accountId).orElseThrow(NotExistDataException::new);
		return accountToAccountDetailResponse(account);
	}

	@Transactional(readOnly = true)
	public AccountDetailResponse getAccountByMemberId(String memberId) {
		Member member = memberService.getMemberById(memberId);
		Account account = Optional.ofNullable(member.getAccount()).orElseThrow(NotExistDataException::new);
		return accountToAccountDetailResponse(account);
	}

	@Transactional(readOnly = true)
	public AccountDetailResponse accountToAccountDetailResponse(Account account) {
		EncryptKey encryptKey = encryptKeyRepository.findByEncryptTarget(EncryptTarget.ACCOUNT)
			.orElseThrow(NotExistDataException::new);
		AES256 aes256 = new AES256(encryptKey.getEncryptKey());
		try {
			return AccountDetailResponse.builder()
				.accountId(account.getId())
				.accountNumber(aes256.decrypt(account.getAccountNumber(), account.getIv()))
				.accountName(aes256.decrypt(account.getAccountName(), account.getIv()))
				.bankName(account.getBankName().getCode())
				.passBookUrl(account.getPassBookUrl())
				.build();
		} catch (Exception e) {
			throw new RuntimeException("AES256 복호화 중 예외발생", e);
		}
	}

	public Long saveAccount(String memberId, AccountSaveRequest accountSaveRequest) {
		EncryptKey encryptKey = encryptKeyRepository.findByEncryptTarget(EncryptTarget.ACCOUNT)
			.orElseThrow(NotExistDataException::new);
		AES256 aes256 = new AES256(encryptKey.getEncryptKey());
		byte[] iv = aes256.generateIv();
		Member member = memberService.getMemberById(memberId);
		Account account;
		try {
			if (member.getAccount() != null) {
				account = member.getAccount();
				account.update(accountSaveRequest, aes256, iv);
			} else {
				account = accountSaveRequest.converterEntity(iv);
				account.setMember(member);
				accountRepository.save(account);
			}
		} catch (Exception e) {
			throw new RuntimeException("AES256 암호화 중 예외발생", e);
		}
		return account.getId();
	}

	public void deleteAccount(String memberId) {
		Member member = memberService.getMemberById(memberId);
		Account account = member.getAccount();
		accountRepository.delete(account);
	}

	public Long getAccountCheck(String memberId, AccountSaveRequest accountSaveRequest) {
		URI uri = createUri(accountSaveRequest);
		log.info(uri.toString());

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setConnection("Keep-Alive");

		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity,
			String.class);

		String[] responseList = response.getBody().split("\\|");

		AccountCheckResponse responseDto = AccountCheckResponse.builder()
			.orderNumber(responseList[0])
			.responseCode(responseList[1])
			.contents(responseList[2])
			.build();

		if (!responseDto.getResponseCode().equals("0000")) {
			log.info("AccountCheckResponse - {}, {}, {}", responseDto.getOrderNumber(), responseDto.getResponseCode(),
				responseDto.getContents());
			throw new BizException("계좌확인 오류입니다.");
		}

		return this.saveAccount(memberId, accountSaveRequest);
	}

	private URI createUri(AccountSaveRequest accountSaveRequest) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NICE_ACCOUNT_URL);
		builder.queryParam("niceUid", NICE_ACCOUNT_SITE_CODE);
		builder.queryParam("svcPwd", NICE_ACCOUNT_PASSWORD);
		builder.queryParam("service", 2); //1: 소유주 확인, 2: 예금주명 확인, 3: 계좌 유효성 확인
		builder.queryParam("svcGbn", 2); //5: 소유주 확인, 2: 예금주명 확인, 4: 계좌 유효성 확인
		builder.queryParam("strGbn", 1); //1: 개인계좌, 2: 법인계좌
		builder.queryParam("strBankCode", accountSaveRequest.getBankName().getCode());
		builder.queryParam("strAccountNo", accountSaveRequest.getAccountNumber());
		builder.queryParam("strNm", URLEncoder.encode(accountSaveRequest.getAccountName(),
			StandardCharsets.UTF_8));
		builder.queryParam("strOrderNo", getDateTime());
		builder.queryParam("inq_rsn", 10); //10 회원가입,20 기존회원가입,30 성인인증,40 비회원확인

		return builder.build(true).toUri();
	}

	private String getDateTime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return now.format(formatter);
	}

}

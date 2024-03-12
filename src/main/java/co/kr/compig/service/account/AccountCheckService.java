package co.kr.compig.service.account;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

import co.kr.compig.api.presentation.account.request.AccountCheckRequest;
import co.kr.compig.api.presentation.account.response.AccountCheckResponse;
import co.kr.compig.common.code.BankCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountCheckService {

	@Value("${api.account.url}")
	private String NICE_ACCOUNT_URL;
	@Value("${api.account.site-code}")
	private String NICE_ACCOUNT_SITE_CODE;
	@Value("${api.account.password}")
	private String NICE_ACCOUNT_PASSWORD;

	public AccountCheckResponse getAccountCheck(AccountCheckRequest accountCheckRequest) {
		URI uri = createUri(accountCheckRequest);
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
			throw new IllegalArgumentException(responseDto.getContents());
		}

		return responseDto;
	}

	private URI createUri(AccountCheckRequest accountCheckRequest) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NICE_ACCOUNT_URL);
		builder.queryParam("niceUid", NICE_ACCOUNT_SITE_CODE);
		builder.queryParam("svcPwd", NICE_ACCOUNT_PASSWORD);
		builder.queryParam("service", accountCheckRequest.getService());
		builder.queryParam("svcGbn", accountCheckRequest.getSvcGbn());
		builder.queryParam("strGbn", accountCheckRequest.getStrGbn());
		builder.queryParam("strBankCode", BankCode.of(accountCheckRequest.getStrBankCode()).getCode());
		builder.queryParam("strAccountNo", accountCheckRequest.getStrAccountNo());
		builder.queryParam("strNm", URLEncoder.encode(accountCheckRequest.getStrNm(),
			StandardCharsets.UTF_8));
		builder.queryParam("strOrderNo", getDateTime());
		builder.queryParam("inq_rsn", accountCheckRequest.getInq_rsn());

		return builder.build(true).toUri();
	}

	private String getDateTime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return now.format(formatter);
	}
}

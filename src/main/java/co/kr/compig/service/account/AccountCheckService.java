package co.kr.compig.service.account;

import co.kr.compig.api.account.dto.AccountCheckRequest;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  public String getAccountCheck(AccountCheckRequest accountCheckRequest) {
    URI uri = createUri(accountCheckRequest);
    log.info(uri.toString());

    RestTemplate restTemplate = new RestTemplate();

    // HTTP 요청 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setConnection("Keep-Alive");

    // HTTP 요청 엔티티 생성
    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    // HTTP POST 요청 보내기
    ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);


//    ResponseEntity<AccountCheckResponse> result = restTemplate.getForEntity(uri, AccountCheckResponse.class);

    return response.getBody();
  }
  private URI createUri(AccountCheckRequest accountCheckRequest) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NICE_ACCOUNT_URL);
    builder.queryParam("niceUid", NICE_ACCOUNT_SITE_CODE);
    builder.queryParam("svcPwd", NICE_ACCOUNT_PASSWORD);
    builder.queryParam("service", accountCheckRequest.getService());
    builder.queryParam("svcGbn", accountCheckRequest.getSvcGbn());
    builder.queryParam("strGbn", accountCheckRequest.getStrGbn());
    builder.queryParam("strBankCode", accountCheckRequest.getStrBankCode().getCode());
    builder.queryParam("strAccountNo", accountCheckRequest.getStrAccountNo());
    builder.queryParam("strNm", URLEncoder.encode(accountCheckRequest.getStrNm(),
        StandardCharsets.UTF_8));
    builder.queryParam("strResId", accountCheckRequest.getStrResId());
    builder.queryParam("strOrderNo", accountCheckRequest.getStrOrderNo());
    builder.queryParam("inq_rsn", accountCheckRequest.getInq_rsn());

    return builder.build(true).toUri();
  }
}

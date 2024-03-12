package co.kr.compig.api.sms;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import co.kr.compig.api.sms.dto.BizPpurioSendResponse;
import co.kr.compig.api.sms.dto.BizPpurioTokenResponse;
import co.kr.compig.api.sms.dto.SmsPayload;

@FeignClient(value = "BizPpurioApi", url = "https://api.bizppurio.com")
public interface BizPpurioApi {

	//consumes는 클라이언트가 서버에게 보내는 데이터 타입을 명시한다.
	//produces는 서버가 클라이언트에게 반환하는 데이터 타입을 명시한다.
	@GetMapping(value = "/v1/token", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	BizPpurioTokenResponse getAccessToken(@RequestHeader("Authorization") String basicToken);

	@PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	BizPpurioSendResponse sendSms(@RequestHeader("Authorization") String bearerToken,
		@RequestBody SmsPayload smsPayload);
}

package co.kr.compig.api.infrastructure.pay;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import co.kr.compig.api.infrastructure.pay.model.CancelPayRequest;
import co.kr.compig.api.infrastructure.pay.model.SmsPayRequest;
import jakarta.ws.rs.core.MediaType;

@FeignClient(name = "payApi", url = "https://api.innopay.co.kr")
public interface PayApi {

	@PostMapping(value = "/api/smsPayApi",
		consumes = MediaType.APPLICATION_JSON)
	ResponseEntity<String> requestSmsPay(
		@RequestBody SmsPayRequest smsPayRequest);

	@PostMapping(value = "/api/cancelApi",
		consumes = MediaType.APPLICATION_JSON)
	ResponseEntity<String> requestCancelPay(
		@RequestBody CancelPayRequest cancelPayRequest);

}

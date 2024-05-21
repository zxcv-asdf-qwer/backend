package co.kr.compig.api.presentation.sms;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.info.sms.SmsService;
import co.kr.compig.api.presentation.sms.request.SmsResultRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "SMS", description = "SMS 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/sms", produces = "application/json")
public class SmsController {

	private final SmsService smsService;

	@PostMapping("/result")
	public void smsSendResultFeedBack(@RequestBody SmsResultRequest smsResultRequest) {
		log.debug(smsResultRequest.toString());
		smsService.smsSendResultFeedBack(smsResultRequest);
	}
}

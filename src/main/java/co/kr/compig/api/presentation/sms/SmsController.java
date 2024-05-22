package co.kr.compig.api.presentation.sms;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.info.sms.SmsService;
import co.kr.compig.api.presentation.sms.request.BizPpurioResultRequest;
import io.swagger.v3.oas.annotations.Operation;
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

	@Operation(summary = "비즈뿌리오 메세지 결과 피드백", hidden = true)
	@PostMapping("/result")
	public void smsSendResultFeedBack(@RequestBody BizPpurioResultRequest bizPpurioResultRequest) {
		log.debug(bizPpurioResultRequest.toString());
		smsService.smsSendResultFeedBack(bizPpurioResultRequest);
	}
}

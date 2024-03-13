package co.kr.compig.api.presentation.sms;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.common.dto.Response;
import co.kr.compig.service.sms.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/sms", produces = "application/json")
public class SmsController {

	private final SmsService smsService;

	@PostMapping("/authentication/{receiverPhoneNumber}")
	public ResponseEntity<Response<?>> sendAuthentication(
		@PathVariable(name = "receiverPhoneNumber") String receiverPhoneNumber) {
		smsService.sendSmsAuthentication(receiverPhoneNumber);
		return ResponseEntity.ok().build();
	}
}

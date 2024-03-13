package co.kr.compig.api.presentation.sms;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.sms.SmsService;
import co.kr.compig.global.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/sms", produces = "application/json")
public class SmsController {

	private final SmsService smsService;

	@PostMapping(path = "/authentication/{receiverPhoneNumber}")
	public ResponseEntity<Response<?>> sendAuthentication(
		@PathVariable(name = "receiverPhoneNumber") String receiverPhoneNumber) {
		smsService.sendSmsAuthentication(receiverPhoneNumber);
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/authentication/{receiverPhoneNumber}")
	public ResponseEntity<Response<?>> getAuthentication(
		@PathVariable(name = "receiverPhoneNumber") String receiverPhoneNumber,
			@RequestParam(name = "authenticationNumber") String authenticationNumber) {
		smsService.getAuthentication(receiverPhoneNumber, authenticationNumber);
		return ResponseEntity.ok().build();
	}
}

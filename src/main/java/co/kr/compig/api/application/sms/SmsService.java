package co.kr.compig.api.application.sms;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import co.kr.compig.api.domain.sms.Sms;
import co.kr.compig.api.domain.sms.SmsRepository;
import co.kr.compig.api.presentation.sms.model.SmsSend;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.model.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

	private final SmsCoreService smsCoreService;
	private final SmsRepository smsRepository;

	public void sendSmsAuthentication(String receiverPhoneNumber) {
		String authenticationNumber = String.valueOf(
			ThreadLocalRandom.current().nextInt(100000, 1000000));

		String contents = "인증 번호 : " + authenticationNumber;
		//TODO 수정
		SmsSend smsSend = SmsSend.builder()
			.receiverPhoneNumber(receiverPhoneNumber)
			.contents(contents)
			.authNumber(authenticationNumber)
			.build();
		try {
			smsRepository.save(smsSend.toEntity());
			smsCoreService.doSendSms(smsSend);
		} catch (Exception e) {
			throw new BizException(ErrorCode.ERROR, "메세지 전송 중 에러가 발생하였습니다.");
		}
	}

	public void getAuthentication(String receiverPhoneNumber, String authenticationNumber) {
		if (receiverPhoneNumber.isEmpty() || authenticationNumber.isEmpty()) {
			throw new BizException("인증 실패");
		}
		Optional<Sms> result = smsRepository.findTopByReceiverPhoneNumberAndRef1OrderByIdDesc(
			receiverPhoneNumber, authenticationNumber);
		result.orElseThrow(() -> new BizException("인증 실패"));
	}
}

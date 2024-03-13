package co.kr.compig.api.application.sms;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import co.kr.compig.api.presentation.sms.request.SmsSend;
import co.kr.compig.global.error.exception.BizException;
import co.kr.compig.global.error.model.ErrorCode;
import co.kr.compig.api.domain.sms.SmsRepository;
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

		String contents = "인증번호 : " + authenticationNumber;
		//TODO 수정
		SmsSend smsSend = new SmsSend();
		smsSend.toBuilder()
			.contents(contents)
			.build();
		try {
			smsRepository.save(smsSend.toEntity());
			smsCoreService.doSendSms(smsSend);
		} catch (Exception e) {
			throw new BizException(ErrorCode.ERROR, "메세지 전송 중 에러가 발생하였습니다.");
		}
	}

}

package co.kr.compig.api.application.sms;

import java.time.ZoneOffset;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import co.kr.compig.api.application.system.AccessKeyService;
import co.kr.compig.api.domain.code.SystemServiceType;
import co.kr.compig.api.infrastructure.sms.BizPpurioApi;
import co.kr.compig.api.infrastructure.sms.model.SmsApiProperties;
import co.kr.compig.api.infrastructure.sms.model.SmsPayload;
import co.kr.compig.api.infrastructure.sms.model.SmsPayload.Sms;
import co.kr.compig.api.presentation.sms.model.SmsSend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsCoreService {

	private final BizPpurioApi bizPpurioApi;
	private final AccessKeyService accessKeyService;
	private final SmsApiProperties smsApiProperties;

	@Async("asyncThreadPoolTaskExecutor")
	public void doSendSms(SmsSend smsSend) {
		String accessToken = accessKeyService.getSecretKey(SystemServiceType.SMS);

		SmsPayload smsPayload = SmsPayload.builder()
			.account(smsApiProperties.getServiceId())
			.type("at") //알림톡
			.from(smsSend.getSenderPhoneNumber()) //보내는 사람
			.to(smsSend.getReceiverPhoneNumber()) //받는 사람
			.country("")
			.refkey(smsSend.getRefkey())
			.userinfo("")
			.resllercode("")
			.sendtime(smsSend.getSendtime() != null ?
				String.valueOf(smsSend.getSendtime().toEpochSecond(ZoneOffset.ofHours(9))) : null
			)
			.content(SmsPayload.Content.builder()
				.at(SmsPayload.At.builder()
					.senderkey("12345")
					.templatecode("template")
					.message(smsSend.getContents())
					.build())
				.build())
			.resend(SmsPayload.Resend.builder()
				.first("sms")
				.build())
			.recontent(SmsPayload.Recontent.builder()
				.sms(Sms.builder()
					.message(smsSend.getContents())
					.build())
				.build())
			.build();

		bizPpurioApi.sendSms("Bearer " + accessToken,
			smsPayload);
	}

}

package co.kr.compig.api.application.info.sms;

import java.time.ZoneOffset;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import co.kr.compig.api.application.info.sms.model.SmsSend;
import co.kr.compig.api.application.system.AccessKeyService;
import co.kr.compig.api.infrastructure.sms.BizPpurioApi;
import co.kr.compig.api.infrastructure.sms.model.BizPpurioApiProperties;
import co.kr.compig.api.infrastructure.sms.model.BizPpurioSendRequest;
import co.kr.compig.api.infrastructure.sms.model.BizPpurioSendRequest.Sms;
import co.kr.compig.global.code.SystemServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsSendService {

	private final BizPpurioApi bizPpurioApi;
	private final AccessKeyService accessKeyService;
	private final BizPpurioApiProperties bizPpurioApiProperties;

	@Async
	public void doSendSms(SmsSend smsSend) {
		String accessToken = accessKeyService.getSecretKey(SystemServiceType.SMS);

		BizPpurioSendRequest bizPpurioSendRequest = BizPpurioSendRequest.builder()
			.account(bizPpurioApiProperties.getServiceId())
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
			.content(BizPpurioSendRequest.Content.builder()
				.at(BizPpurioSendRequest.At.builder()
					.senderkey("12345")
					.templatecode("template")
					.message(smsSend.getContents())
					.build())
				.build())
			.resend(BizPpurioSendRequest.Resend.builder()
				.first("sms")
				.build())
			.recontent(BizPpurioSendRequest.Recontent.builder()
				.sms(Sms.builder()
					.message(smsSend.getContents())
					.build())
				.build())
			.build();

		bizPpurioApi.sendSms("Bearer " + accessToken,
			bizPpurioSendRequest);
	}

}

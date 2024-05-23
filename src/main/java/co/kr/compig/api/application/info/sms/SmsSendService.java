package co.kr.compig.api.application.info.sms;

import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
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
				String.valueOf(smsSend.getSendtime().toEpochSecond(ZoneOffset.ofHours(9))) : null)
			.content(BizPpurioSendRequest.Content.builder()
				.at(BizPpurioSendRequest.At.builder()
					.senderkey(bizPpurioApiProperties.getSenderKey())
					.templatecode(smsSend.getInfoTemplate().getTemplateCode())
					.message(smsSend.getContents())
					.build())
				.build())
			.resend(BizPpurioSendRequest.Resend.builder()
				.first(smsSend.getContents().getBytes(StandardCharsets.UTF_8).length > 90 ? "lms" : "sms")
				.build())
			.recontent(getRecontent(smsSend))
			.build();

		bizPpurioApi.sendSms("Bearer " + accessToken, bizPpurioSendRequest);
	}

	private BizPpurioSendRequest.Recontent getRecontent(SmsSend smsSend) {
		// 문자열의 바이트 크기 확인
		byte[] contentBytes = smsSend.getContents().getBytes(StandardCharsets.UTF_8);
		BizPpurioSendRequest.Recontent recontent;
		if (contentBytes.length > 90) {
			recontent = BizPpurioSendRequest.Recontent.builder()
				.lms(BizPpurioSendRequest.Lms.builder().message(smsSend.getContents()).build())
				.build();
		} else {
			recontent = BizPpurioSendRequest.Recontent.builder()
				.sms(Sms.builder().message(smsSend.getContents()).build())
				.build();
		}
		return recontent;
	}
}

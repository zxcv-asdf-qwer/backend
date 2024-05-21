package co.kr.compig.api.application.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.internal.util.CollectionsUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.info.push.FirebasePushService;
import co.kr.compig.api.application.info.push.model.MessageDto;
import co.kr.compig.api.application.info.push.model.NotificationRequest;
import co.kr.compig.api.application.info.sms.SmsService;
import co.kr.compig.api.application.info.sms.SmsTemplateService;
import co.kr.compig.api.application.info.sms.model.SmsSend;
import co.kr.compig.api.domain.sms.SmsTemplate;
import co.kr.compig.api.infrastructure.sms.model.BizPpurioApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InfoService {

	private final SmsService smsService;
	private final FirebasePushService firebasePushService;
	private final SmsTemplateService smsTemplateService;
	private final BizPpurioApiProperties bizPpurioApiProperties;

	@Async
	public void send(MessageDto wingMessageDto) {
		sendMessage(List.of(wingMessageDto));
	}

	@Async
	public void sendAll(List<MessageDto> wingMessageDtoList) {
		sendMessage(wingMessageDtoList);
	}

	private void sendMessage(List<MessageDto> messageDtoList) {
		List<SmsSend> smsSends = new ArrayList<>();
		messageDtoList.forEach(messageDto -> {
			if (hasPushMessage(messageDto)) {
				log.info("### Send push");
				SmsTemplate bySmsTemplateType = smsTemplateService.getBySmsTemplateType(
					messageDto.getNoticeCode().getPushTemplate());
				messageDto.getTargetTokens().forEach(targetToken -> {
					firebasePushService.sendMessageTo(NotificationRequest.builder()
						.deviceToken(targetToken)
						.title(messageDto.getNoticeCode().getTitle())
						.body(getContents(bySmsTemplateType.getContents(), messageDto.getData()))
						.build());
				});
			}

			if (hasSmsAlertMessage(messageDto)) {
				log.info("### Send alert");
				SmsTemplate bySmsTemplateType = smsTemplateService.getBySmsTemplateType(
					messageDto.getNoticeCode().getAlertTemplate());
				smsSends.add(getSmsSend((messageDto), bySmsTemplateType));
			}
		});

		if (CollectionsUtils.hasItems(smsSends)) {
			smsService.create(smsSends);
		}
	}

	private SmsSend getSmsSend(MessageDto messageDto, SmsTemplate smsTemplate) {
		return SmsSend.builder()
			.senderPhoneNumber(bizPpurioApiProperties.getSenderPhoneNumber())
			.receiverPhoneNumber(messageDto.getPhoneNumber())
			.contents(getContents(smsTemplate.getContents(), messageDto.getData()))
			.smsTemplate(smsTemplate)
			.build();
	}

	private String getContents(String contents, Map<String, Object> data) {
		if (StringUtils.isEmpty(contents)) {
			return null;
		}
		for (String key : data.keySet()) {
			if (ObjectUtils.isNotEmpty(data.get(key))) {
				contents = contents.replaceAll("\\{" + key + "}", String.valueOf(data.get(key)));
			}
		}
		return contents;
	}

	private boolean hasSmsAlertMessage(MessageDto messageDto) {
		return ObjectUtils.isNotEmpty(messageDto.getNoticeCode().getAlertTemplate()) && StringUtils.isNotEmpty(
			messageDto.getPhoneNumber());
	}

	private boolean hasPushMessage(MessageDto messageDto) {
		return ObjectUtils.isNotEmpty(messageDto.getNoticeCode().getPushTemplate()) && CollectionUtils.isNotEmpty(
			messageDto.getTargetTokens());
	}
}

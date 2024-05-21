package co.kr.compig.api.application.info.sms;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import co.kr.compig.api.application.info.push.model.NoticeCode;
import co.kr.compig.api.application.info.sms.model.SmsSend;
import co.kr.compig.api.domain.sms.Sms;
import co.kr.compig.api.domain.sms.SmsRepository;
import co.kr.compig.api.domain.sms.SmsTemplate;
import co.kr.compig.api.presentation.sms.request.SmsRedirectRequest;
import co.kr.compig.global.error.exception.BizException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

	private final SmsSendService smsSendService;
	private final SmsRepository smsRepository;
	private final SmsTemplateService smsTemplateService;

	public void create(List<SmsSend> smsSends) {
		List<Sms> smsList = smsSends.stream()
			.map(smsSend -> {
				try {
					smsSendService.doSendSms(smsSend);
				} catch (FeignException e) {
					log.error(e.getMessage());
				}
				return smsSend.toEntity();
			})
			.collect(Collectors.toList());
		smsRepository.saveAll(smsList);
	}

	public void getAuthenticationTopByReceiverPhoneNumber(String receiverPhoneNumber, String authenticationNumber,
		NoticeCode noticeCode) {
		SmsTemplate smsTemplate = smsTemplateService.getBySmsTemplateType(noticeCode.getAlertTemplate());
		Optional.ofNullable(smsTemplate.getSmsTemplateType()).orElseThrow(() -> new BizException("인증 실패"));
		Optional<Sms> result = smsRepository.findTopByReceiverPhoneNumberAndSmsTemplateOrderByIdDesc(
			receiverPhoneNumber,
			smsTemplate);
		result.orElseThrow(() -> new BizException("인증 실패"));

		LocalDateTime createdOn = result.get().getCreatedAndModified().getCreatedOn();
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(createdOn, now);
		if (duration.toMinutes() > 1) {
			throw new BizException("인증번호가 만료되었습니다.");
		}

		String contents = result.get().getContents();
		String extractedNumber = extractNumberFromString(contents);
		if (!extractedNumber.equals(authenticationNumber)) {
			throw new BizException("인증번호가 일치하지 않습니다.");
		}
	}

	private String extractNumberFromString(String text) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group();
		}
		throw new BizException("인증번호를 찾을 수 없습니다.");
	}

	public void smsSendResultFeedBack(SmsRedirectRequest smsRedirectRequest) {

	}
}

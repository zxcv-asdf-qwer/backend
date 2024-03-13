package co.kr.compig.api.application.sms;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.kr.compig.api.domain.code.SmsTemplateType;
import co.kr.compig.api.domain.sms.SmsTemplate;
import co.kr.compig.api.domain.sms.SmsTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsTemplateService {

	private final SmsTemplateRepository smsTemplateRepository;

	public SmsTemplate getSmsTemplateDetail(SmsTemplateType smsTemplateType) {
		Optional<SmsTemplate> bySmsTemplateType = smsTemplateRepository.findBySmsTemplateType(
			smsTemplateType);
		return null;
	}
}

package co.kr.compig.service.sms;

import co.kr.compig.common.code.SmsTemplateType;
import co.kr.compig.domain.sms.SmsTemplate;
import co.kr.compig.domain.sms.SmsTemplateRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

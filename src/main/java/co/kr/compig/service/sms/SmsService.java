package co.kr.compig.service.sms;

import co.kr.compig.api.sms.dto.SmsSend;
import co.kr.compig.common.exception.BizException;
import co.kr.compig.common.exception.dto.ErrorCode;
import co.kr.compig.domain.sms.SmsRepository;
import co.kr.compig.service.system.AccessKeyService;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

  private final AccessKeyService accessKeyService;
  private final SmsCoreService smsCoreService;
  private final SmsRepository smsRepository;

  public void sendSmsAuthentication(SmsSend smsSend) {
    String authenticationNumber = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    String contents = "인증번호 : " + authenticationNumber;
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

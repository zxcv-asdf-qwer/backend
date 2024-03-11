package co.kr.compig.service.sms;

import co.kr.compig.api.sms.BizPpurioApi;
import co.kr.compig.api.sms.SmsApiProperties;
import co.kr.compig.api.sms.dto.SmsPayload;
import co.kr.compig.api.sms.dto.SmsPayload.Content;
import co.kr.compig.api.sms.dto.SmsPayload.Recontent;
import co.kr.compig.api.sms.dto.SmsPayload.Resend;
import co.kr.compig.api.sms.dto.SmsPayload.Sms;
import co.kr.compig.api.sms.dto.SmsSend;
import co.kr.compig.common.code.SystemServiceType;
import co.kr.compig.service.system.AccessKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsCoreService {

  private final BizPpurioApi bizPpurioApi;
  private final AccessKeyService accessKeyService;
  private final SmsApiProperties smsApiProperties;

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
        .sendtime("") //String.valueOf(smsSend.getSendtime().toEpochSecond(ZoneOffset.ofHours(9)))
        .content(Content.builder()
            .sms(Sms.builder()
                .message(smsSend.getContents())
                .build())
            .build())
        .resend(Resend.builder()
            .first("sms")
            .build())
        .recontent(Recontent.builder()
            .sms(Sms.builder()
                .message(smsSend.getContents())
                .build())
            .build())
        .build();

    bizPpurioApi.sendSms("Bearer " + accessToken,
        smsPayload);
  }

}

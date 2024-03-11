package co.kr.compig.service.sms;

import co.kr.compig.api.sms.dto.BizPpurioApi;
import co.kr.compig.api.sms.dto.SmsPayload;
import co.kr.compig.api.sms.dto.SmsSend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsCoreService {

  private final BizPpurioApi bizPpurioApi;

  public void doSendSms(SmsSend smsSend) {
    SmsPayload.builder()
        .account("hychung")
        .type("sms")
        .from("0234305001")
        .to("01080011434")
        .country("")
        .refkey(smsSend.getRefkey())
        .userinfo("your_userinfo")
        .resllercode("your_resellercode")
        .sendtime("your_sendtime")
        .content(SmsPayload.Content.builder()
            .sms(SmsPayload.Sms.builder()
                .message("Your message here")
                .build())
            .build())
        .resend(SmsPayload.Resend.builder()
            .first("sms")
            .build())
        .recontent(SmsPayload.Recontent.builder()
            .sms(SmsPayload.Sms.builder()
                .message("SMS 대체 발송 메시지")
                .build())
            .build())
        .build();
  }

}

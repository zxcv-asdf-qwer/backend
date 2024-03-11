package co.kr.compig.service.sms;

import co.kr.compig.api.sms.dto.SmsAuthenticationRequest;
import co.kr.compig.common.exception.BizException;
import co.kr.compig.common.exception.dto.ErrorCode;
import co.kr.compig.service.system.AccessKeyService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

  private final AccessKeyService accessKeyService;
  @Value("${api.sms.service-id}")
  private String smsServiceId;


  public void sendSmsAuthentication(SmsAuthenticationRequest smsAuthenticationRequest) {
    // 먼저 SmsAuthenticationRequest 객체를 생성합니다.
    SmsAuthenticationRequest request = SmsAuthenticationRequest.builder()
        .senderPhoneNumber("보내는 전화번호")
        .receiverPhoneNumber("받는 전화번호")
        .contents("메시지 내용")
        .refkey("참조 키")
        .authenticationNumber("인증 번호")
        .build();


    try {

    } catch (Exception e) {
      throw new BizException(ErrorCode.ERROR, "메세지 전송 중 에러가 발생하였습니다.");
    }
  }

  public static String getNow() {
    // 현재 날짜와 시간을 구합니다.
    LocalDateTime now = LocalDateTime.now();
    // 날짜와 시간을 원하는 형태의 문자열로 포맷합니다.
    String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

    // 10자리 난수를 생성합니다.
    Random random = new Random();
    StringBuilder serialCode = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      int digit = random.nextInt(10); // 0에서 9 사이의 난수를 생성합니다.
      serialCode.append(digit);
    }

    // 포맷된 날짜와 시간 문자열과 난수를 결합하여 반환합니다.
    return formattedDateTime + serialCode; //'202403080731385069491909524'
  }
}

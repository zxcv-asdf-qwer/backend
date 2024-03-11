package co.kr.compig.api.sms.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class SmsAuthenticationRequest {

  private String authenticationNumber; //인증번호

}

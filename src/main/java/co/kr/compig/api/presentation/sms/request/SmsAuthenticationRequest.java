package co.kr.compig.api.presentation.sms.request;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class SmsAuthenticationRequest {

	private String authenticationNumber; //인증번호

}

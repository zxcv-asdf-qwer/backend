package co.kr.compig.api.presentation.sms.response;

import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class SmsResponse extends BaseAudit {
	private Long smsId;
	private String contents; //내용
	private String receivedPhoneNumber; //수신번호
	private String sendResult; //수신결과
}

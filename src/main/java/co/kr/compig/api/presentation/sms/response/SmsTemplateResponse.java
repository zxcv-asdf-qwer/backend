package co.kr.compig.api.presentation.sms.response;

import co.kr.compig.global.code.SmsTemplateType;
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
public class SmsTemplateResponse extends BaseAudit {
	private Long smsTemplateId;
	private SmsTemplateType smsTemplateType; //SMS 템플릿 코드
	private String contents; //내용
}

package co.kr.compig.api.application.info.push.model;

import co.kr.compig.global.code.SmsTemplateType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeCode {
	PHONE_VERIFICATION("휴대폰 인증번호 전송", SmsTemplateType.PHONE_VERIFICATION_TEMPLATE,
		SmsTemplateType.PHONE_VERIFICATION_TEMPLATE);

	private final String title;
	private final SmsTemplateType pushTemplate;
	private final SmsTemplateType alertTemplate;
}

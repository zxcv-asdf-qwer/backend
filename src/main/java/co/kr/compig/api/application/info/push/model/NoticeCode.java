package co.kr.compig.api.application.info.push.model;

import co.kr.compig.global.code.SmsTemplateType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeCode {
	PHONE_VERIFICATION("휴대폰 인증번호 전송", SmsTemplateType.PHONE_VERIFICATION_TEMPLATE,
		SmsTemplateType.PHONE_VERIFICATION_TEMPLATE),
	NEW_CREATE_ORDER("새 공고 등록 알림", SmsTemplateType.NEW_CREATE_ORDER,
		SmsTemplateType.NEW_CREATE_ORDER),
	;

	private final String title;
	private final SmsTemplateType pushTemplate;
	private final SmsTemplateType alertTemplate;
}

package co.kr.compig.api.application.info.push.model;

import co.kr.compig.global.code.infoTemplateType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeCode {
	PHONE_VERIFICATION("휴대폰 인증번호 전송", infoTemplateType.PHONE_VERIFICATION_TEMPLATE,
		infoTemplateType.PHONE_VERIFICATION_TEMPLATE),
	NEW_CREATE_ORDER("새 공고 등록 알림", infoTemplateType.NEW_CREATE_ORDER,
		infoTemplateType.NEW_CREATE_ORDER),
	;

	private final String title;
	private final infoTemplateType pushTemplate;
	private final infoTemplateType alertTemplate;
}

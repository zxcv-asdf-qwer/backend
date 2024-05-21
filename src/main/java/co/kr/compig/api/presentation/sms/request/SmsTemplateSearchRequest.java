package co.kr.compig.api.presentation.sms.request;

import co.kr.compig.global.code.SmsTemplateType;
import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class SmsTemplateSearchRequest extends PageableRequest {
	private SmsTemplateType smsTemplateType; //SMS 템플릿 코드
	private String templateCode; //카카오 알림톡 템플릿 코드
	private String contents; //내용
}

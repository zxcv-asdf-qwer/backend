package co.kr.compig.api.presentation.sms.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class SmsTemplateSearchRequest extends PageableRequest {
	private String test;
}

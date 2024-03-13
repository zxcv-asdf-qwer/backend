package co.kr.compig.api.presentation.sms.request;

import co.kr.compig.api.domain.sms.SmsTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsTemplateCreateRequest {
	private String test;
	public SmsTemplate converterEntity() {
		return SmsTemplate.builder().build();
	}
}

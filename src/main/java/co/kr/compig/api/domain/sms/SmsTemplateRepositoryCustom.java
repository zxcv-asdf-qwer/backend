package co.kr.compig.api.domain.sms;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.sms.request.SmsTemplateSearchRequest;
import co.kr.compig.api.presentation.sms.response.SmsTemplateResponse;

@Repository
public interface SmsTemplateRepositoryCustom {
	Page<SmsTemplateResponse> findPage(SmsTemplateSearchRequest smsTemplateSearchRequest);
}

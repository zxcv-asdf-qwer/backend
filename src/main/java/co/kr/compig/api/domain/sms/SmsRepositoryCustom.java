package co.kr.compig.api.domain.sms;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.sms.request.SmsSearchRequest;
import co.kr.compig.api.presentation.sms.response.SmsResponse;

@Repository
public interface SmsRepositoryCustom {
	Page<SmsResponse> findPage(SmsSearchRequest request);
}

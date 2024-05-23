package co.kr.compig.api.domain.sms;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.infotemplate.request.InfoTemplateSearchRequest;
import co.kr.compig.api.presentation.infotemplate.response.InfoTemplateResponse;

@Repository
public interface InfoTemplateRepositoryCustom {
	Page<InfoTemplateResponse> findPage(InfoTemplateSearchRequest infoTemplateSearchRequest);
}

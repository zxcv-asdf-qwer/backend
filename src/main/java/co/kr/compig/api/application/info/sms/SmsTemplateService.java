package co.kr.compig.api.application.info.sms;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import co.kr.compig.api.domain.sms.SmsTemplate;
import co.kr.compig.api.domain.sms.SmsTemplateRepository;
import co.kr.compig.api.domain.sms.SmsTemplateRepositoryCustom;
import co.kr.compig.api.presentation.sms.request.SmsTemplateCreateRequest;
import co.kr.compig.api.presentation.sms.request.SmsTemplateSearchRequest;
import co.kr.compig.api.presentation.sms.request.SmsTemplateUpdateRequest;
import co.kr.compig.api.presentation.sms.response.SmsTemplateResponse;
import co.kr.compig.global.code.SmsTemplateType;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsTemplateService {

	private final SmsTemplateRepository smsTemplateRepository;
	private final SmsTemplateRepositoryCustom smsTemplateRepositoryCustom;

	public SmsTemplate getBySmsTemplateType(SmsTemplateType smsTemplateType) {
		Optional<SmsTemplate> bySmsTemplateType = smsTemplateRepository.findTopBySmsTemplateTypeOrderByIdDesc(
			smsTemplateType);
		return bySmsTemplateType.orElse(new SmsTemplate());
	}

	public Long create(SmsTemplateCreateRequest smsTemplateCreateRequest) {
		SmsTemplate smsTemplate = smsTemplateCreateRequest.converterEntity();
		return smsTemplateRepository.save(smsTemplate).getId();
	}

	public SmsTemplateResponse getById(Long smsTemplateId) {
		SmsTemplate smsTemplate = smsTemplateRepository.findById(smsTemplateId).orElseThrow(NotExistDataException::new);
		return smsTemplate.toSmsTemplateDetailResponse();
	}

	public Page<SmsTemplateResponse> getPage(SmsTemplateSearchRequest smsTemplateSearchRequest) {
		return smsTemplateRepositoryCustom.findPage(smsTemplateSearchRequest);
	}

	public Long updateById(Long smsTemplateId, SmsTemplateUpdateRequest smsTemplateUpdateRequest) {
		SmsTemplate smsTemplate = smsTemplateRepository.findById(smsTemplateId).orElseThrow(NotExistDataException::new);
		smsTemplate.update(smsTemplateUpdateRequest);
		return smsTemplate.getId();
	}

	public void deleteById(Long smsTemplateId) {
		SmsTemplate smsTemplate = smsTemplateRepository.findById(smsTemplateId).orElseThrow(NotExistDataException::new);
		smsTemplateRepository.delete(smsTemplate);
	}

}

package co.kr.compig.api.application.info;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.sms.InfoTemplate;
import co.kr.compig.api.domain.sms.InfoTemplateRepository;
import co.kr.compig.api.domain.sms.InfoTemplateRepositoryCustom;
import co.kr.compig.api.presentation.info.request.InfoTemplateCreateRequest;
import co.kr.compig.api.presentation.info.request.InfoTemplateSearchRequest;
import co.kr.compig.api.presentation.info.request.InfoTemplateUpdateRequest;
import co.kr.compig.api.presentation.info.response.InfoTemplateResponse;
import co.kr.compig.global.code.infoTemplateType;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InfoTemplateService {

	private final InfoTemplateRepository infoTemplateRepository;
	private final InfoTemplateRepositoryCustom infoTemplateRepositoryCustom;

	@Transactional(readOnly = true)
	public InfoTemplate getByInfoTemplateType(infoTemplateType infoTemplateType) {
		Optional<InfoTemplate> byInfoTemplateType = infoTemplateRepository.findTopByInfoTemplateTypeOrderByIdDesc(
			infoTemplateType);
		return byInfoTemplateType.orElse(new InfoTemplate());
	}

	public Long create(InfoTemplateCreateRequest infoTemplateCreateRequest) {
		InfoTemplate infoTemplate = infoTemplateCreateRequest.converterEntity();
		return infoTemplateRepository.save(infoTemplate).getId();
	}

	public InfoTemplateResponse getById(Long infoTemplateId) {
		InfoTemplate infoTemplate = infoTemplateRepository.findById(infoTemplateId)
			.orElseThrow(NotExistDataException::new);
		return infoTemplate.toInfoTemplateDetailResponse();
	}

	@Transactional(readOnly = true)
	public Page<InfoTemplateResponse> getPage(InfoTemplateSearchRequest infoTemplateSearchRequest) {
		return infoTemplateRepositoryCustom.findPage(infoTemplateSearchRequest);
	}

	public Long updateById(Long infoTemplateId, InfoTemplateUpdateRequest infoTemplateUpdateRequest) {
		InfoTemplate infoTemplate = infoTemplateRepository.findById(infoTemplateId)
			.orElseThrow(NotExistDataException::new);
		infoTemplate.update(infoTemplateUpdateRequest);
		return infoTemplate.getId();
	}

	public void deleteById(Long infoTemplateId) {
		InfoTemplate infoTemplate = infoTemplateRepository.findById(infoTemplateId)
			.orElseThrow(NotExistDataException::new);
		infoTemplateRepository.delete(infoTemplate);
	}

}

package co.kr.compig.api.application.settle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.domain.settle.SettleRepository;
import co.kr.compig.api.domain.settle.SettleRepositoryCustom;
import co.kr.compig.api.presentation.settle.request.SettleCreateRequest;
import co.kr.compig.api.presentation.settle.request.SettleSearchRequest;
import co.kr.compig.api.presentation.settle.request.SettleUpdateRequest;
import co.kr.compig.api.presentation.settle.response.SettleResponse;
import co.kr.compig.global.dto.pagination.PageResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SettleService {
	private final SettleRepository settleRepository;
	private final SettleRepositoryCustom settleRepositoryCustom;

	public Long createSettle(SettleCreateRequest settleCreateRequest) {
		return settleRepository.save(settleCreateRequest.converterEntity()).getId();
	}

	@Transactional(readOnly = true)
	public PageResponse<SettleResponse> getSettlePage(SettleSearchRequest settleSearchRequest, Pageable pageable) {
		Page<SettleResponse> page = settleRepositoryCustom.getPage(settleSearchRequest, pageable);
		return new PageResponse<>(page.getContent(), pageable, page.getTotalElements());
	}

	public Long update(Long settleId, SettleUpdateRequest settleUpdateRequest) {
		Settle settle = settleRepository.findById(settleId).orElseThrow(NotExistDataException::new);
		settle.update(settleUpdateRequest);
		return settle.getId();
	}
}

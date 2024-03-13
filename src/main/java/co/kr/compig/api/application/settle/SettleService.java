package co.kr.compig.api.application.settle;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.presentation.settle.request.SettleCreateRequest;
import co.kr.compig.api.presentation.settle.response.SettleResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import co.kr.compig.api.domain.settle.Settle;
import co.kr.compig.api.domain.settle.SettleGroup;
import co.kr.compig.api.domain.settle.SettleGroupRepository;
import co.kr.compig.api.domain.settle.SettleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SettleService {
	private final SettleRepository settleRepository;
	private final SettleGroupRepository settleGroupRepository;

	public Long createSettle(List<SettleCreateRequest> settleCreateRequests) {
		SettleGroup settleGroup = settleGroupRepository.save(new SettleGroup());
		settleCreateRequests.stream()
			.map(settleCreateRequest -> settleCreateRequest.converterEntity(settleGroup))
			.forEach(settleRepository::save);
		return settleGroup.getId();
	}

	@Transactional(readOnly = true)
	public List<SettleResponse> getSettleList(Long settleGroupId) {
		List<Settle> settles = settleRepository.findBySettleGroupId(settleGroupId);
		return settles.stream()
			.map(Settle::toSettleResponse)
			.collect(Collectors.toList());
	}

	public Long updateSettleUseYn(Long settleId) {
		Settle settle = settleRepository.findById(settleId).orElseThrow(NotExistDataException::new);
		settle.setUseYn();
		return settle.getId();
	}
}

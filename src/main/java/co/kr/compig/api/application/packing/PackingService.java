package co.kr.compig.api.application.packing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.order.CareOrderRepository;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.packing.PackingRepository;
import co.kr.compig.api.domain.packing.PackingRepositoryCustom;
import co.kr.compig.api.domain.settle.SettleGroup;
import co.kr.compig.api.domain.settle.SettleGroupRepository;
import co.kr.compig.api.presentation.packing.request.PackingCreateRequest;
import co.kr.compig.api.presentation.packing.request.PackingSearchRequest;
import co.kr.compig.api.presentation.packing.request.PackingUpdateRequest;
import co.kr.compig.api.presentation.packing.response.PackingDetailResponse;
import co.kr.compig.api.presentation.packing.response.PackingResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PackingService {

	private final PackingRepository packingRepository;
	private final PackingRepositoryCustom packingRepositoryCustom;
	private final CareOrderRepository careOrderRepository;
	private final SettleGroupRepository settleGroupRepository;

	public Long createPacking(PackingCreateRequest packingCreateRequest) {
		CareOrder careOrder = careOrderRepository.findById(packingCreateRequest.getCareOrderId()).orElseThrow(
			NotExistDataException::new);
		SettleGroup settleGroup = settleGroupRepository.findById(packingCreateRequest.getSettleGroupId())
			.orElseThrow(NotExistDataException::new);
		Packing packing = packingCreateRequest.converterEntity(careOrder, settleGroup);
		return packingRepository.save(packing).getId();
	}

	@Transactional(readOnly = true)
	public Page<PackingResponse> pageListPacking(PackingSearchRequest packingSearchRequest, Pageable pageable) {
		return packingRepositoryCustom.findPage(packingSearchRequest, pageable);
	}

	public PackingDetailResponse getPacking(Long packingId) {
		Packing packing = packingRepository.findById(packingId).orElseThrow(NotExistDataException::new);
		return packing.toPackingDetailResponse();
	}

	public Long updatePacking(Long packingId, PackingUpdateRequest packingUpdateRequest) {
		Packing packing = packingRepository.findById(packingId).orElseThrow(NotExistDataException::new);
		packing.update(packingUpdateRequest);
		return packing.getId();
	}

	public void deletePacking(Long packingId) {
		Packing packing = packingRepository.findById(packingId).orElseThrow(NotExistDataException::new);
		packingRepository.delete(packing);
	}
}

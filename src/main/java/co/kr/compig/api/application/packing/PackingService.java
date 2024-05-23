package co.kr.compig.api.application.packing;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.packing.PackingRepository;
import co.kr.compig.api.presentation.packing.request.PackingCreateRequest;
import co.kr.compig.api.presentation.packing.request.PackingUpdateRequest;
import co.kr.compig.api.presentation.packing.response.PackingDetailResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PackingService {

	private final PackingRepository packingRepository;
	private final CareOrderService careOrderService;

	public Long createPacking(PackingCreateRequest packingCreateRequest) {
		CareOrder careOrder = careOrderService.getCareOrderById(packingCreateRequest.getCareOrderId());
		Packing packing = packingCreateRequest.converterEntity(careOrder);
		return packingRepository.save(packing).getId();
	}

	@Transactional(readOnly = true)
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

	@Transactional(readOnly = true)
	public Packing getPackingById(Long packingId) {
		return packingRepository.findById(packingId).orElseThrow(NotExistDataException::new);
	}
}

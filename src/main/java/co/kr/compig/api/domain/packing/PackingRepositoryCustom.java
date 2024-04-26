package co.kr.compig.api.domain.packing;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import co.kr.compig.api.presentation.packing.request.PackingSearchRequest;
import co.kr.compig.api.presentation.packing.response.PackingResponse;
import co.kr.compig.global.code.ApplyStatus;
import co.kr.compig.global.code.OrderStatus;

public interface PackingRepositoryCustom {
	Page<PackingResponse> findPage(PackingSearchRequest packingSearchRequest);

	List<Packing> findByEndDateTimeLessThanEqualAndOrderStatusAndApplyStatusAndWalletIsNull(
		LocalDateTime endDateTime, OrderStatus orderStatus, ApplyStatus applyStatus);
}

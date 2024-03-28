package co.kr.compig.api.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.response.CareOrderResponse;

@Repository
public interface CareOrderRepositoryCustom {

	Page<CareOrderResponse> findPage(CareOrderSearchRequest careOrderSearchRequest, Pageable pageable);

	Slice<CareOrderResponse> findAllByCondition(CareOrderSearchRequest careOrderSearchRequest, Pageable pageable);
}

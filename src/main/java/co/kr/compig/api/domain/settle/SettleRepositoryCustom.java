package co.kr.compig.api.domain.settle;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.settle.request.SettleSearchRequest;
import co.kr.compig.api.presentation.settle.response.SettleResponse;

@Repository
public interface SettleRepositoryCustom {
	Page<SettleResponse> getPage(SettleSearchRequest settleSearchRequest);
}

package co.kr.compig.api.domain.packing;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.packing.request.PackingSearchRequest;
import co.kr.compig.api.presentation.packing.response.PackingResponse;

@Repository
public interface PackingRepositoryCustom {
	Page<PackingResponse> findPage(PackingSearchRequest packingSearchRequest);
}

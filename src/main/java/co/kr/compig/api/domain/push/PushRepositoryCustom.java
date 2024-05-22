package co.kr.compig.api.domain.push;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.push.request.PushSearchRequest;
import co.kr.compig.api.presentation.push.response.PushResponse;

@Repository
public interface PushRepositoryCustom {
	Page<PushResponse> findPage(PushSearchRequest request);
}

package co.kr.compig.api.domain.apply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.apply.request.ApplySearchRequest;
import co.kr.compig.api.presentation.apply.response.ApplyResponse;

@Repository
public interface ApplyRepositoryCustom {
	Page<ApplyResponse> getApplyPage(ApplySearchRequest applySearchRequest, Pageable pageable);

	Slice<ApplyResponse> getApplySlice(ApplySearchRequest applySearchRequest, Pageable pageable);
}

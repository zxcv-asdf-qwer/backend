package co.kr.compig.api.domain.terms;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.terms.request.TermsSearchRequest;
import co.kr.compig.api.presentation.terms.response.TermsResponse;
import co.kr.compig.global.code.TermsType;

@Repository
public interface TermsRepositoryCustom {
	Page<TermsResponse> getTermsPage(TermsSearchRequest termsSearchRequest);

	Slice<TermsResponse> getTermsSlice(TermsSearchRequest termsSearchRequest, Pageable pageable);

	Map<TermsType, List<TermsResponse>> getTermsList(TermsSearchRequest termsSearchRequest);
}

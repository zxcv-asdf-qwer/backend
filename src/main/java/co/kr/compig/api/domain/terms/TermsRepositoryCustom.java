package co.kr.compig.api.domain.terms;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.terms.request.TermsSearchRequest;
import co.kr.compig.api.presentation.terms.response.TermsResponse;

@Repository
public interface TermsRepositoryCustom {
	Page<TermsResponse> getTermsPage(TermsSearchRequest termsSearchRequest);

	Slice<TermsResponse> getTermsSlice(TermsSearchRequest termsSearchRequest, Pageable pageable);

	List<TermsResponse> getTermsList(TermsSearchRequest termsSearchRequest);
}

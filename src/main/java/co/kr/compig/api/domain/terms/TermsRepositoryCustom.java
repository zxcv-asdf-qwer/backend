package co.kr.compig.api.domain.terms;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.terms.request.TermsSearchRequest;
import co.kr.compig.api.presentation.terms.response.TermsResponse;

@Repository
public interface TermsRepositoryCustom {
	Page<TermsResponse> findPage(TermsSearchRequest termsSearchRequest, Pageable pageable);
}

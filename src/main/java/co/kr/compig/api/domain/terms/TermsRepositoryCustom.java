package co.kr.compig.api.domain.terms;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.terms.request.TermsSearchRequest;
import co.kr.compig.api.presentation.terms.response.TermsListResponse;
import co.kr.compig.api.presentation.terms.response.TermsResponse;
import co.kr.compig.global.code.TermsType;

@Repository
public interface TermsRepositoryCustom {
	Page<TermsResponse> getTermsPage(TermsSearchRequest termsSearchRequest);

	Map<TermsType, List<TermsListResponse>> getTermsList();
}

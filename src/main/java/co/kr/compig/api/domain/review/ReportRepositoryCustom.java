package co.kr.compig.api.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.review.request.ReportSearchRequest;
import co.kr.compig.api.presentation.review.response.ReportResponse;

@Repository
public interface ReportRepositoryCustom {
	Page<ReportResponse> getReportPage(ReportSearchRequest reportSearchRequest);
}

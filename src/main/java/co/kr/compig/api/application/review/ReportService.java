package co.kr.compig.api.application.review;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.domain.review.Report;
import co.kr.compig.api.domain.review.ReportRepository;
import co.kr.compig.api.domain.review.ReportRepositoryCustom;
import co.kr.compig.api.domain.review.Review;
import co.kr.compig.api.domain.review.ReviewRepository;
import co.kr.compig.api.presentation.review.request.ReportCreateRequest;
import co.kr.compig.api.presentation.review.request.ReportSearchRequest;
import co.kr.compig.api.presentation.review.response.ReportResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

	private final ReviewRepository reviewRepository;
	private final ReportRepository reportRepository;
	private final ReportRepositoryCustom reportRepositoryCustom;
	private final ReviewService reviewService;

	public Long createReportGuardian(Long reviewId, ReportCreateRequest reportCreateRequest) {
		Review review = reviewService.getReviewById(reviewId);
		Report report = reportCreateRequest.converterEntity(review);
		return reportRepository.save(report).getId();
	}

	public Page<ReportResponse> getReportPage(ReportSearchRequest reportSearchRequest) {
		return reportRepositoryCustom.getReportPage(reportSearchRequest);
	}

	public ReportResponse getReport(Long reportId) {
		Report report = reportRepository.findById(reportId).orElseThrow(NotExistDataException::new);
		return report.toResponse();
	}
}

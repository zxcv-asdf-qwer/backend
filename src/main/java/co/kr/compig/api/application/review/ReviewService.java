package co.kr.compig.api.application.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;
import co.kr.compig.api.domain.review.Report;
import co.kr.compig.api.domain.review.ReportRepository;
import co.kr.compig.api.domain.review.ReportRepositoryCustom;
import co.kr.compig.api.domain.review.Review;
import co.kr.compig.api.domain.review.ReviewRepository;
import co.kr.compig.api.domain.review.ReviewRepositoryCustom;
import co.kr.compig.api.presentation.review.request.ReportCreateRequest;
import co.kr.compig.api.presentation.review.request.ReportSearchRequest;
import co.kr.compig.api.presentation.review.request.ReviewCreateRequest;
import co.kr.compig.api.presentation.review.request.ReviewSearchRequest;
import co.kr.compig.api.presentation.review.request.ReviewUpdateRequest;
import co.kr.compig.api.presentation.review.response.ReportDetailResponse;
import co.kr.compig.api.presentation.review.response.ReportResponse;
import co.kr.compig.api.presentation.review.response.ReviewDetailResponse;
import co.kr.compig.api.presentation.review.response.ReviewResponse;
import co.kr.compig.global.dto.pagination.PageResponse;
import co.kr.compig.global.dto.pagination.SliceResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewRepositoryCustom reviewRepositoryCustom;
	private final ReportRepository reportRepository;
	private final ReportRepositoryCustom reportRepositoryCustom;
	private final MemberService memberService;
	private final CareOrderService careOrderService;

	public Long createReviewGuardian(ReviewCreateRequest reviewCreateRequest) {
		Member member = memberService.getMemberById(reviewCreateRequest.getMemberId());
		CareOrder careOrder = careOrderService.getCareOrderById(reviewCreateRequest.getCareOrderId());
		Review review = reviewCreateRequest.converterEntity(member, careOrder);
		reviewRepository.save(review);
		return review.getId();
	}

	@Transactional(readOnly = true)
	public SliceResponse<ReviewResponse> pageListReviewCursor(ReviewSearchRequest reviewSearchRequest,
		Pageable pageable) {
		Slice<ReviewResponse> slice = reviewRepositoryCustom.findAllByCondition(reviewSearchRequest, pageable);
		return new SliceResponse<>(slice.getContent(), pageable, slice.hasNext());
	}

	@Transactional(readOnly = true)
	public ReviewDetailResponse getReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(NotExistDataException::new);
		return review.toReviewDetailResponse();
	}

	public Long updateReview(Long reviewId, ReviewUpdateRequest reviewUpdateRequest) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(NotExistDataException::new);
		review.update(reviewUpdateRequest);
		return review.getId();
	}

	public void deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(NotExistDataException::new);
		reviewRepository.delete(review);
	}

	public Long createReportGuardian(Long reviewId, ReportCreateRequest reportCreateRequest) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(NotExistDataException::new);
		Report report = reportCreateRequest.converterEntity(review);
		return reportRepository.save(report).getId();
	}

	public Page<ReportResponse> getReportPage(ReportSearchRequest reportSearchRequest) {
		return reportRepositoryCustom.getReportPage(reportSearchRequest);
	}

	public ReportDetailResponse getReport(Long reportId) {
		Report report = reportRepository.findById(reportId).orElseThrow(NotExistDataException::new);
		Member member = memberService.getMemberById(report.getCreatedAndModified().getCreatedBy());
		return report.toReportDetailResponse(member);
	}
}
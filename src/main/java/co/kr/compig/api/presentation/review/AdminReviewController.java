package co.kr.compig.api.presentation.review;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.review.ReviewService;
import co.kr.compig.api.presentation.review.request.ReportSearchRequest;
import co.kr.compig.api.presentation.review.response.ReportDetailResponse;
import co.kr.compig.api.presentation.review.response.ReportResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 리뷰", description = "리뷰 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/review", produces = "application/json")
public class AdminReviewController {

	private final ReviewService reviewService;

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{reviewId}")
	public ResponseEntity<Response<?>> deleteReview(
		@PathVariable(name = "reviewId") Long reviewId
	) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "신고 조회", description = "페이징")
	@GetMapping("/report")
	public ResponseEntity<PageResponse<ReportResponse>> getReportPage(
		@ParameterObject @ModelAttribute @Valid ReportSearchRequest reportSearchRequest,
		@ParameterObject Pageable pageable
	) {
		return ResponseEntity.ok(reviewService.getReportPage(reportSearchRequest, pageable));
	}

	//리뷰 자세히 보기
	@Operation(summary = "상세 조회")
	@GetMapping(path = "/report/{reportId}")
	public ResponseEntity<Response<ReportDetailResponse>> getReport(
		@PathVariable(name = "reportId") Long reportId
	) {
		return ResponseEntity.ok(Response.<ReportDetailResponse>builder()
			.data(reviewService.getReport(reportId))
			.build());
	}

}

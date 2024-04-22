package co.kr.compig.api.presentation.review;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.review.ReportService;
import co.kr.compig.api.presentation.review.request.ReportSearchRequest;
import co.kr.compig.api.presentation.review.response.ReportResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 리뷰 신고", description = "리뷰 신고 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/report", produces = "application/json")
public class AdminReportController {

	private final ReportService reportService;

	@Operation(summary = "신고 조회", description = "페이징")
	@GetMapping
	public ResponseEntity<PageResponse> getReportPage(
		@ParameterObject @ModelAttribute ReportSearchRequest reportSearchRequest
	) {
		Page<ReportResponse> page = reportService.getReportPage(reportSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());

	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{reportId}")
	public ResponseEntity<Response<ReportResponse>> getReport(
		@PathVariable(name = "reportId") Long reportId
	) {
		return ResponseEntity.ok(Response.<ReportResponse>builder()
			.data(reportService.getReport(reportId))
			.build());
	}
}

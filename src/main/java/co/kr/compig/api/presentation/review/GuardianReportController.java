package co.kr.compig.api.presentation.review;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.review.ReportService;
import co.kr.compig.api.presentation.review.request.ReportCreateRequest;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "보호자 리뷰", description = "리뷰 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/guardian/report", produces = "application/json")
public class GuardianReportController {

	private final ReportService reportService;

	@Operation(summary = "신고하기")
	@PostMapping(path = "/{reviewId}")
	public ResponseEntity<Response<?>> createReport(
		@PathVariable(name = "reviewId") Long reviewId,
		@RequestBody @Valid ReportCreateRequest reportCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("reportId", reportService.createReportGuardian(reviewId, reportCreateRequest)))
			.build());
	}
}

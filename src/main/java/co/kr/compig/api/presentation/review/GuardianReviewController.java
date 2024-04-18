package co.kr.compig.api.presentation.review;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.review.ReviewService;
import co.kr.compig.api.presentation.review.request.ReviewCreateRequest;
import co.kr.compig.api.presentation.review.request.ReviewSearchRequest;
import co.kr.compig.api.presentation.review.request.ReviewUpdateRequest;
import co.kr.compig.api.presentation.review.response.ReviewDetailResponse;
import co.kr.compig.api.presentation.review.response.ReviewResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
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
@RequestMapping(path = "/guardian/review", produces = "application/json")
public class GuardianReviewController {

	private final ReviewService reviewService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createReview(
		@ParameterObject @RequestBody @Valid ReviewCreateRequest reviewCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("reviewId", reviewService.createReviewGuardian(reviewCreateRequest)))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<SliceResponse<ReviewResponse>> pageListReview(
		@ParameterObject @ModelAttribute @Valid ReviewSearchRequest reviewSearchRequest,
		Pageable pageable
	) {
		return ResponseEntity.ok(reviewService.pageListReviewCursor(reviewSearchRequest, pageable));
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{reviewId}")
	public ResponseEntity<Response<ReviewDetailResponse>> getReview(
		@PathVariable(name = "reviewId") Long reviewId
	) {
		return ResponseEntity.ok(Response.<ReviewDetailResponse>builder()
			.data(reviewService.getReview(reviewId))
			.build());
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping(path = "/{reviewId}")
	public ResponseEntity<Response<?>> updateReview(
		@PathVariable(name = "reviewId") Long reviewId,
		@RequestBody @Valid ReviewUpdateRequest reviewUpdateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("reviewId", reviewService.updateReview(reviewId, reviewUpdateRequest)))
			.build());
	}
}

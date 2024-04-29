package co.kr.compig.api.presentation.review;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.review.ReviewService;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
	@PutMapping(path = "/delete/{reviewId}")
	public ResponseEntity<Response<?>> deleteReview(
		@PathVariable(name = "reviewId") Long reviewId
	) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.ok().build();
	}

}

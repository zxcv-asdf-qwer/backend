package co.kr.compig.api.presentation.apply;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.apply.ApplyService;
import co.kr.compig.api.presentation.apply.request.ApplyCreateRequest;
import co.kr.compig.api.presentation.apply.request.ApplySearchRequest;
import co.kr.compig.api.presentation.apply.response.ApplyDetailResponse;
import co.kr.compig.api.presentation.apply.response.ApplyResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "간병인 지원", description = "지원 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/partner/applies", produces = "application/json")
public class PartnerApplyController {
	private final ApplyService applyService;

	@Operation(summary = "간병인 지원하기")
	@PostMapping("/orders/{orderId}")
	public ResponseEntity<Response<?>> createApply(@PathVariable Long orderId,
		@RequestBody @Valid ApplyCreateRequest applyCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("applyId", applyService.createApplyByGuardian(orderId, applyCreateRequest)))
			.build());
	}

	@Operation(summary = "orderId 로 간병인 지원 목록 조회(커서)")
	@GetMapping("/orders/{orderId}")
	public ResponseEntity<SliceResponse<ApplyResponse>> getApplySlice(@PathVariable Long orderId,
		@ParameterObject @ModelAttribute @Valid ApplySearchRequest applySearchRequest) {
		return ResponseEntity.ok(applyService.getApplySlice(orderId, applySearchRequest));
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/orders/{orderId}/memberId/{partnerId}")
	public ResponseEntity<Response<ApplyDetailResponse>> getApply(
		@PathVariable(name = "orderId") Long orderId, @PathVariable(name = "partnerId") String memberId) {
		return ResponseEntity.ok(Response.<ApplyDetailResponse>builder()
			.data(applyService.getApply(orderId, memberId))
			.build());
	}

	@Operation(summary = "지원 취소 시키기")
	@DeleteMapping(path = "/matching-cancel/orders/{orderId}/memberId/{partnerId}")
	public ResponseEntity<Response<?>> cancelApply(
		@PathVariable(name = "orderId") Long orderId, @PathVariable(name = "partnerId") String partnerId) {
		applyService.deleteApply(orderId, partnerId);
		return ResponseEntity.ok().build();
	}
}

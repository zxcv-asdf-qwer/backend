package co.kr.compig.api.presentation.apply;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.apply.ApplyService;
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

@Tag(name = "보호자 지원", description = "지원 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/guardian/applies", produces = "application/json")
public class GuardianApplyController {
	private final ApplyService applyService;

	@Operation(summary = "목록 조회(커서)")
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

	@Operation(summary = "매칭완료")
	@PutMapping(path = "/matching-complete/orders/{orderId}/memberId/{partnerId}")
	public ResponseEntity<Response<?>> updateMatchingComplete(
		@PathVariable(name = "orderId") Long orderId, @PathVariable(name = "partnerId") String memberId) {
		applyService.updateMatchingComplete(orderId, memberId);
		return ResponseEntity.ok().build();
	}

}

package co.kr.compig.api.presentation.apply;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.apply.ApplyService;
import co.kr.compig.api.presentation.apply.request.ApplyCreateRequest;
import co.kr.compig.api.presentation.apply.response.ApplyDetailResponse;
import co.kr.compig.api.presentation.apply.response.ApplyResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 간병인 지원", description = "지원 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/applys", produces = "application/json")
public class AdminApplyController {

	private final ApplyService applyService;

	@Operation(summary = "간병인 지원 시키기")
	@PostMapping("/orders/{orderId}")
	public ResponseEntity<Response<?>> createApply(@PathVariable Long orderId,
		@RequestBody @Valid ApplyCreateRequest applyCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("applyId", applyService.createApplyByAdmin(orderId, applyCreateRequest)))
			.build());
	}

	@Operation(summary = "orderId 로 간병인 지원 목록 조회", description = "페이징 없이")
	@GetMapping("/orders/{orderId}")
	public ResponseEntity<List<ApplyResponse>> getApplies(@PathVariable Long orderId) {
		return ResponseEntity.ok(applyService.getApplies(orderId));
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{applyId}")
	public ResponseEntity<Response<ApplyDetailResponse>> getApply(
		@PathVariable(name = "applyId") Long applyId) {
		return ResponseEntity.ok(Response.<ApplyDetailResponse>builder()
			.data(applyService.getApply(applyId))
			.build());
	}

	@Operation(summary = "매칭완료")
	@PutMapping(path = "/matching-complete/{applyId}")
	public ResponseEntity<Response<?>> updateMatchingComplete(
		@PathVariable(name = "applyId") Long applyId) {
		applyService.updateMatchingComplete(applyId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "매칭대기")
	@PutMapping(path = "/matching-wait/{applyId}")
	public ResponseEntity<Response<?>> updateMatchingWait(
		@PathVariable(name = "applyId") Long applyId) {
		applyService.updateMatchingWait(applyId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "지원 취소 시키기")
	@DeleteMapping(path = "/{applyId}")
	public ResponseEntity<Response<?>> deleteApply(
		@PathVariable(name = "applyId") Long applyId) {
		applyService.deleteApply(applyId);
		return ResponseEntity.ok().build();
	}
}

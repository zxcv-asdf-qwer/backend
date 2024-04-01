package co.kr.compig.api.presentation.apply;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.apply.ApplyService;
import co.kr.compig.api.presentation.apply.request.ApplyCreateRequest;
import co.kr.compig.api.presentation.apply.request.ApplyUpdateRequest;
import co.kr.compig.api.presentation.apply.response.ApplyDetailResponse;
import co.kr.compig.api.presentation.apply.response.ApplyResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 지원", description = "지원 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/apply", produces = "application/json")
public class AdminApplyController {

	private final ApplyService applyService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createApply(
		@ModelAttribute @Valid ApplyCreateRequest applyCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("applyId", applyService.createApply(applyCreateRequest)))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<PageResponse<ApplyResponse>> pageListCareOrder(
		Pageable pageable
	) {
		Page<ApplyResponse> page = applyService.pageListApply(pageable);
		PageResponse<ApplyResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{applyId}")
	public ResponseEntity<Response<ApplyDetailResponse>> getApply(
		@PathVariable(name = "applyId") Long applyId
	) {
		return ResponseEntity.ok(Response.<ApplyDetailResponse>builder()
			.data(applyService.getApply(applyId))
			.build());
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping(path = "/{applyId}")
	public ResponseEntity<Response<?>> updateApply(@PathVariable(name = "applyId") Long applyId,
		@RequestBody @Valid ApplyUpdateRequest applyUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("applyId", applyService.updateApply(applyId, applyUpdateRequest)))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{applyId}")
	public ResponseEntity<Response<?>> deleteApply(@PathVariable(name = "applyId") Long applyId) {
		applyService.deleteApply(applyId);
		return ResponseEntity.ok().build();
	}
}

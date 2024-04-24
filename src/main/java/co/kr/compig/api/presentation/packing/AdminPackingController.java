package co.kr.compig.api.presentation.packing;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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

import co.kr.compig.api.application.packing.PackingService;
import co.kr.compig.api.presentation.packing.request.PackingCreateRequest;
import co.kr.compig.api.presentation.packing.request.PackingSearchRequest;
import co.kr.compig.api.presentation.packing.request.PackingUpdateRequest;
import co.kr.compig.api.presentation.packing.response.PackingDetailResponse;
import co.kr.compig.api.presentation.packing.response.PackingResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 간병 하루", description = "간병 하루 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/packing", produces = "application/json")
public class AdminPackingController {

	private final PackingService packingService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createPacking(
		@RequestBody @Valid PackingCreateRequest packingCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("packingId", packingService.createPacking(packingCreateRequest)))
			.build());
	}

	@Operation(summary = "조회", description = "페이징")
	@GetMapping
	public ResponseEntity<PageResponse> getPackingPage(
		@ParameterObject @ModelAttribute PackingSearchRequest packingSearchRequest) {
		Page<PackingResponse> page = packingService.getPackingPage(packingSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{packingId}")
	public ResponseEntity<Response<PackingDetailResponse>> getPacking(
		@PathVariable(name = "packingId") Long packingId) {
		return ResponseEntity.ok(Response.<PackingDetailResponse>builder()
			.data(packingService.getPacking(packingId))
			.build());
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping(path = "/{packingId}")
	public ResponseEntity<Response<?>> updatePacking(
		@PathVariable(name = "packingId") Long packingId,
		@RequestBody @Valid PackingUpdateRequest packingUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("packingId", packingService.updatePacking(packingId, packingUpdateRequest)))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{packingId}")
	public ResponseEntity<Response<?>> deletePacking(
		@PathVariable(name = "packingId") Long packingId) {
		packingService.deletePacking(packingId);
		return ResponseEntity.ok().build();
	}
}

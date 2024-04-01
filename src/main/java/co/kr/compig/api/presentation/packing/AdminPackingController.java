package co.kr.compig.api.presentation.packing;

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

import co.kr.compig.api.application.packing.PackingService;
import co.kr.compig.api.presentation.packing.request.PackingCreateRequest;
import co.kr.compig.api.presentation.packing.request.PackingSearchRequest;
import co.kr.compig.api.presentation.packing.request.PackingUpdateRequest;
import co.kr.compig.api.presentation.packing.response.PackingDetailResponse;
import co.kr.compig.api.presentation.packing.response.PackingResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/packing", produces = "application/json")
public class AdminPackingController {

	private final PackingService packingService;

	@PostMapping
	public ResponseEntity<Response<?>> createPacking(
		@ModelAttribute @Valid PackingCreateRequest packingCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("packingId", packingService.createPacking(packingCreateRequest)))
			.build());
	}

	@GetMapping
	public ResponseEntity<PageResponse<PackingResponse>> pageListPacking(
		@ModelAttribute @Valid PackingSearchRequest packingSearchRequest, Pageable pageable
	) {
		Page<PackingResponse> page = packingService.pageListPacking(packingSearchRequest, pageable);
		PageResponse<PackingResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@GetMapping(path = "/{packingId}")
	public ResponseEntity<Response<PackingDetailResponse>> getPacking(
		@PathVariable(name = "packingId") Long packingId
	) {
		return ResponseEntity.ok(Response.<PackingDetailResponse>builder()
			.data(packingService.getPacking(packingId))
			.build());
	}

	@PutMapping(path = "/{packingId}")
	public ResponseEntity<Response<?>> updatePacking(@PathVariable(name = "packingId") Long packingId,
		@RequestBody @Valid PackingUpdateRequest packingUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("packingId", packingService.updatePacking(packingId, packingUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{packingId}")
	public ResponseEntity<Response<?>> deletePacking(@PathVariable(name = "packingId") Long packingId) {
		packingService.deletePacking(packingId);
		return ResponseEntity.ok().build();
	}
}

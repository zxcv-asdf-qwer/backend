package co.kr.compig.api.presentation.order;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.presentation.order.request.CareOrderCreateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.request.CareOrderUpdateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.CareOrderResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "유저 간병 공고", description = "간병 공고 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/care-order", produces = "application/json")
public class UserCareOrderController {

	private final CareOrderService careOrderService;

	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createCareOrder(
		@ParameterObject @ModelAttribute @Valid CareOrderCreateRequest careOrderCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("careOrderId", careOrderService.createCareOrderUser(careOrderCreateRequest)))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<SliceResponse<CareOrderResponse>> pageListCareOrder(
		@ParameterObject @ModelAttribute @Valid CareOrderSearchRequest careOrderSearchRequest,
		Pageable pageable
	) {
		Slice<CareOrderResponse> slice = careOrderService.pageListCareOrderCursor(careOrderSearchRequest, pageable);
		SliceResponse<CareOrderResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable,
			slice.hasNext());
		return ResponseEntity.ok(sliceResponse);
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{careOrderId}")
	public ResponseEntity<Response<CareOrderDetailResponse>> getCareOrder(
		@PathVariable(name = "careOrderId") Long careOrderId
	) {
		return ResponseEntity.ok(Response.<CareOrderDetailResponse>builder()
			.data(careOrderService.getCareOrder(careOrderId))
			.build());
	}

	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "정보 수정하기")
	@PutMapping(path = "/{careOrderId}")
	public ResponseEntity<Response<?>> updateCareOrder(
		@PathVariable(name = "careOrderId") Long careOrderId,
		@RequestBody @Valid CareOrderUpdateRequest careOrderUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("careOrderId", careOrderService.updateCareOrder(careOrderId, careOrderUpdateRequest)))
			.build());
	}

	@SecurityRequirement(name = "Bearer Authentication")
	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{careOrderId}")
	public ResponseEntity<Response<?>> deleteCareOrder(
		@PathVariable(name = "careOrderId") Long careOrderId) {
		careOrderService.deleteCareOrder(careOrderId);
		return ResponseEntity.ok().build();
	}
}

package co.kr.compig.api.presentation.order;

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

import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.presentation.order.request.AdminCareOrderCreateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.request.CareOrderUpdateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.CareOrderResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/care-order", produces = "application/json")
public class AdminCareOrderController {

	private final CareOrderService careOrderService;

	@PostMapping
	public ResponseEntity<Response<?>> createCareOrder(
		@ModelAttribute @Valid AdminCareOrderCreateRequest adminCareOrderCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("careOrderId", careOrderService.createCareOrderAdmin(adminCareOrderCreateRequest)))
			.build());
	}

	@GetMapping
	public ResponseEntity<PageResponse<CareOrderResponse>> pageListCareOrder(
		@ModelAttribute @Valid CareOrderSearchRequest careOrderSearchRequest, Pageable pageable
	) {
		Page<CareOrderResponse> page = careOrderService.pageListCareOrder(careOrderSearchRequest, pageable);
		PageResponse<CareOrderResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@GetMapping(path = "/{careOrderId}")
	public ResponseEntity<Response<CareOrderDetailResponse>> getCareOrder(
		@PathVariable(name = "careOrderId") Long careOrderId
	) {
		return ResponseEntity.ok(Response.<CareOrderDetailResponse>builder()
			.data(careOrderService.getCareOrder(careOrderId))
			.build());
	}

	@PutMapping(path = "/{careOrderId}")
	public ResponseEntity<Response<?>> updateCareOrder(@PathVariable(name = "careOrderId") Long careOrderId,
		@RequestBody @Valid CareOrderUpdateRequest careOrderUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("careOrderId", careOrderService.updateCareOrder(careOrderId, careOrderUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{careOrderId}")
	public ResponseEntity<Response<?>> deleteCareOrder(@PathVariable(name = "careOrderId") Long careOrderId) {
		careOrderService.deleteCareOrder(careOrderId);
		return ResponseEntity.ok().build();
	}
}
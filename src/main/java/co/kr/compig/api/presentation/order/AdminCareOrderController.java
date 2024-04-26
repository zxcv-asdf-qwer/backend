package co.kr.compig.api.presentation.order;

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

import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.presentation.order.request.CareOrderCalculateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderCreateRequest;
import co.kr.compig.api.presentation.order.request.CareOrderExtensionsRequest;
import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.request.FamilyCareOrderCreateRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.CareOrderPageResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 간병 공고", description = "간병 공고 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/orders", produces = "application/json")
public class AdminCareOrderController {

	private final CareOrderService careOrderService;

	@Operation(summary = "간병 공고 등록")
	@PostMapping("/member/{memberId}")
	public ResponseEntity<Response<?>> createCareOrder(@PathVariable String memberId,
		@RequestBody @Valid CareOrderCreateRequest careOrderCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("orderId", careOrderService.createCareOrderAdmin(memberId, careOrderCreateRequest)))
			.build());
	}

	@Operation(summary = "가족 간병공고 생성하기")
	@PostMapping("/member/{memberId}/family")
	public ResponseEntity<Response<?>> createFamilyCareOrder(@PathVariable String memberId,
		@RequestBody @Valid FamilyCareOrderCreateRequest familyCareOrderCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("orderUrl",
				careOrderService.createFamilyCareOrderAdmin(memberId, familyCareOrderCreateRequest)))
			.build());
	}

	@Operation(summary = "간병 리스트 보여주기")
	@GetMapping("/pages")
	public ResponseEntity<PageResponse> pageListCareOrder(
		@ParameterObject @ModelAttribute CareOrderSearchRequest careOrderSearchRequest) {
		Page<CareOrderPageResponse> page = careOrderService.pageListCareOrder(
			careOrderSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Operation(summary = "간병비 계산")
	@GetMapping(path = "/calculate")
	public ResponseEntity<Response<?>> getCareOrderCalculate(
		@ParameterObject @ModelAttribute CareOrderCalculateRequest careOrderCalculateRequest
	) {
		return ResponseEntity.ok(Response.<Map<String, Integer>>builder()
			.data(Map.of("totalPrice", careOrderService.getCareOrderCalculate(careOrderCalculateRequest)))
			.build());
	}

	@Operation(summary = "간병 공고 상세 조회")
	@GetMapping(path = "/{orderId}")
	public ResponseEntity<Response<CareOrderDetailResponse>> getCareOrder(
		@PathVariable(name = "orderId") Long orderId
	) {
		return ResponseEntity.ok(Response.<CareOrderDetailResponse>builder()
			.data(careOrderService.getCareOrder(orderId))
			.build());
	}

	@Operation(summary = "간병 공고 연장하기")
	@PutMapping(path = "/{orderId}/extensions")
	public ResponseEntity<Response<?>> extensionsCareOrder(
		@PathVariable(name = "orderId") Long orderId,
		@RequestBody @Valid CareOrderExtensionsRequest careOrderExtensionsRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("orderUrl", careOrderService.extensionsCareOrder(orderId, careOrderExtensionsRequest)))
			.build());
	}

	@Operation(summary = "간병 취소하기")
	@DeleteMapping(path = "/{orderId}")
	public ResponseEntity<Response<?>> cancelCareOrder(
		@PathVariable(name = "orderId") Long orderId) {
		careOrderService.cancelCareOrder(orderId);
		return ResponseEntity.ok().build();
	}
}

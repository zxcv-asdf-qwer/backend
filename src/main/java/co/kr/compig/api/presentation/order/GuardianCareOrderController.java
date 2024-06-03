package co.kr.compig.api.presentation.order;

import java.util.Map;

import org.flywaydb.core.internal.util.CollectionsUtils;
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
import co.kr.compig.api.presentation.order.request.CareOrderExtensionsRequest;
import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.request.CareOrderTerminateRequest;
import co.kr.compig.api.presentation.order.request.FamilyCareOrderCreateRequest;
import co.kr.compig.api.presentation.order.response.GuardianCareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.UserCareOrderResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "보호자 간병 공고", description = "간병 공고 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/guardian/orders", produces = "application/json")
public class GuardianCareOrderController {

	private final CareOrderService careOrderService;

	@Operation(summary = "일반 간병공고 생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createCareOrder(
		@RequestBody @Valid CareOrderCreateRequest careOrderCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("orderId", careOrderService.createCareOrderGuardian(careOrderCreateRequest)))
			.build());
	}

	@Operation(summary = "가족 간병공고 생성하기")
	@PostMapping("/family")
	public ResponseEntity<Response<?>> createFamilyCareOrder(
		@RequestBody @Valid FamilyCareOrderCreateRequest familyCareOrderCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("orderUrl", careOrderService.createFamilyCareOrderGuardian(familyCareOrderCreateRequest)))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<SliceResponse<UserCareOrderResponse>> pageListCareOrder(
		@ParameterObject @ModelAttribute @Valid CareOrderSearchRequest careOrderSearchRequest,
		Pageable pageable
	) {
		Slice<UserCareOrderResponse> slice = careOrderService.pageListCareOrderCursor(careOrderSearchRequest,
			pageable);
		SliceResponse<UserCareOrderResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable,
			slice.hasNext(),
			CollectionsUtils.hasItems(slice.getContent()) ?
				slice.getContent().get(slice.getContent().size() - 1).getId().toString() : "");
		return ResponseEntity.ok(sliceResponse);
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{orderId}")
	public ResponseEntity<Response<GuardianCareOrderDetailResponse>> getCareOrder(
		@PathVariable(name = "orderId") Long careOrderId
	) {
		return ResponseEntity.ok(Response.<GuardianCareOrderDetailResponse>builder()
			.data(careOrderService.getGuardianCareOrder(careOrderId))
			.build());
	}

	@Operation(summary = "간병 공고 연장하기")
	@PutMapping(path = "/{orderId}/extensions")
	public ResponseEntity<Response<?>> extensionsCareOrder(
		@PathVariable(name = "orderId") Long orderId,
		@RequestBody @Valid CareOrderExtensionsRequest careOrderExtensionsRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, String>>builder()
			.data(Map.of("orderUrl", careOrderService.extensionsCareOrder(orderId, careOrderExtensionsRequest)))
			.build());
	}

	@Operation(summary = "간병 종료하기, 취소하기")
	@DeleteMapping(path = "/{orderId}")
	public ResponseEntity<Response<?>> cancelCareOrder(
		@PathVariable(name = "orderId") Long careOrderId,
		@ModelAttribute @Valid CareOrderTerminateRequest careOrderTerminateRequest) {
		careOrderService.cancelCareOrderForGuardian(careOrderId, careOrderTerminateRequest);
		return ResponseEntity.ok().build();
	}
}

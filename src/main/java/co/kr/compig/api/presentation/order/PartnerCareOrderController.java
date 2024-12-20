package co.kr.compig.api.presentation.order;

import org.flywaydb.core.internal.util.CollectionsUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.order.CareOrderService;
import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.UserCareOrderResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "간병인 간병 공고", description = "간병 공고 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/partner/orders", produces = "application/json")
public class PartnerCareOrderController {

	private final CareOrderService careOrderService;

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
	public ResponseEntity<Response<CareOrderDetailResponse>> getCareOrder(
		@PathVariable(name = "orderId") Long careOrderId
	) {
		return ResponseEntity.ok(Response.<CareOrderDetailResponse>builder()
			.data(careOrderService.getCareOrder(careOrderId))
			.build());
	}
}

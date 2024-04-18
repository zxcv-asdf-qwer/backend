package co.kr.compig.api.presentation.settle;

import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.settle.SettleService;
import co.kr.compig.api.presentation.settle.request.SettleCreateRequest;
import co.kr.compig.api.presentation.settle.request.SettleSearchRequest;
import co.kr.compig.api.presentation.settle.response.SettleResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "수수료 관리", description = "수수료 관리 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/settle", produces = "application/json")
public class AdminSettleController {
	private final SettleService settleService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createSettle(
		@RequestBody @Valid SettleCreateRequest settleCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("settleGroupId", settleService.createSettle(settleCreateRequest)))
			.build());
	}

	@Operation(summary = "페이지 조회")
	@GetMapping("/pages")
	public ResponseEntity<PageResponse> pageListSettle(
		@ParameterObject @ModelAttribute SettleSearchRequest settleSearchRequest) {
		Page<SettleResponse> page = settleService.getSettlePage(settleSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

}

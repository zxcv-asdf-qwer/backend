package co.kr.compig.api.presentation.settle;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.settle.SettleService;
import co.kr.compig.api.presentation.settle.request.SettleCreateRequest;
import co.kr.compig.api.presentation.settle.response.SettleResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 간병 요소", description = "간병 요소 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/settle", produces = "application/json")
public class AdminSettleController {
	private final SettleService settleService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> createSettle(
		@RequestBody @Valid List<SettleCreateRequest> settleCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("settleGroupId", settleService.createSettle(settleCreateRequest)))
			.build());
	}

	@Operation(summary = "상세 조회 - settleGroupId")
	@GetMapping(path = "/{settleGroupId}")
	public ResponseEntity<Response<List<SettleResponse>>> pageListSettle(
		@PathVariable(name = "settleGroupId") Long settleGroupId) {
		return ResponseEntity.ok().body(Response.<List<SettleResponse>>builder()
			.data(settleService.getSettleList(settleGroupId)).build());
	}

	@Operation(summary = "상세 조회")
	@PutMapping(path = "/{settleId}")
	public ResponseEntity<Response<?>> deleteSettle(@PathVariable(name = "settleId") Long settleId) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("settleId", settleService.updateSettleUseYn(settleId)))
			.build());
	}

}

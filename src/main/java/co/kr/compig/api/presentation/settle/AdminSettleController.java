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

import co.kr.compig.api.presentation.settle.request.SettleCreateRequest;
import co.kr.compig.api.presentation.settle.response.SettleResponse;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.settle.SettleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/settle", produces = "application/json")
public class AdminSettleController {
	private final SettleService settleService;

	@PostMapping
	public ResponseEntity<Response<?>> createSettle(
		@RequestBody @Valid List<SettleCreateRequest> settleCreateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("settleGroupId", settleService.createSettle(settleCreateRequest)))
			.build());
	}

	@GetMapping("/{settleGroupId}")
	public ResponseEntity<Response<List<SettleResponse>>> pageListSettle(
		@PathVariable(name = "settleGroupId") Long settleGroupId) {
		return ResponseEntity.ok().body(Response.<List<SettleResponse>>builder()
			.data(settleService.getSettleList(settleGroupId)).build());
	}

	@PutMapping("/{settleId}")
	public ResponseEntity<Response<?>> deleteSettle(@PathVariable(name = "settleId") Long settleId) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("settleId", settleService.updateSettleUseYn(settleId)))
			.build());
	}

}

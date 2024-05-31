package co.kr.compig.api.presentation.settle;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.settle.SettleService;
import co.kr.compig.api.presentation.settle.response.SettleResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "간병인 수수료 정보", description = "수수료 정보 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/guardian/settle", produces = "application/json")
public class GuardianSettleController {
	private final SettleService settleService;

	@Operation(summary = "가장 최근 수수료 조회")
	@GetMapping()
	public ResponseEntity<Response<SettleResponse>> getSettle() {
		return ResponseEntity.ok(Response.<SettleResponse>builder()
			.data(settleService.getRecentSettle().toSettleResponse())
			.build());
	}

}

package co.kr.compig.api.presentation.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.app.AppVersionService;
import co.kr.compig.api.presentation.app.response.AppVersionResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "사용자 앱버전", description = "앱버전 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/apps", produces = "application/json")
public class UserAppVersionController {

	private final AppVersionService appVersionService;

	@Operation(summary = "OS 타입으로 최근 앱버전 조회")
	@GetMapping("/recent/{os-type}")
	public ResponseEntity<Response<AppVersionResponse>> getRecentByOsType(
		@PathVariable(name = "os-type") String osType) {
		return ResponseEntity.ok(Response.<AppVersionResponse>builder()
			.data(appVersionService.getRecentByOsType(osType))
			.build());
	}
}

package co.kr.compig.api.presentation.app;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.app.AppVersionService;
import co.kr.compig.api.presentation.app.request.AppVersionCreateRequest;
import co.kr.compig.api.presentation.app.request.AppVersionUpdateRequest;
import co.kr.compig.api.presentation.app.response.AppVersionResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 앱버전", description = "앱버전 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/pv/apps", produces = "application/json")
public class AppVersionController {

	private final AppVersionService appVersionService;

	@Operation(summary = "생성하기")
	@PostMapping
	public ResponseEntity<Response<?>> create(
		@RequestBody @Valid AppVersionCreateRequest request) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("appVersionId", appVersionService.create(request)))
			.build());
	}

	@Operation(summary = "OS 타입으로 최근 앱버전 조회")
	@GetMapping("/recent/{os-type}")
	public ResponseEntity<Response<AppVersionResponse>> getRecentByOsType(
		@PathVariable(name = "os-type") String osType) {
		return ResponseEntity.ok(Response.<AppVersionResponse>builder()
			.data(appVersionService.getRecentByOsType(osType))
			.build());
	}

	@Operation(summary = "상세 조회")
	@GetMapping("/{app-id}")
	public ResponseEntity<Response<AppVersionResponse>> getById(
		@PathVariable(name = "app-id") Long appId) {
		return ResponseEntity.ok(Response.<AppVersionResponse>builder()
			.data(appVersionService.getById(appId))
			.build());
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping("/{app-id}")
	public ResponseEntity<Response<?>> updateById(
		@PathVariable(name = "app-id") Long appId,
		@RequestBody @Valid AppVersionUpdateRequest request
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("appVersionId", appVersionService.updateById(appId, request)))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping("/{app-id}")
	public ResponseEntity<Response<?>> deleteById(
		@PathVariable(name = "app-id") Long appId) {
		appVersionService.deleteById(appId);
		return ResponseEntity.ok().build();
	}
}

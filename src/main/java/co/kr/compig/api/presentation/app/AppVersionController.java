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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/apps", produces = "application/json")
public class AppVersionController {

	private final AppVersionService appVersionService;

	@PostMapping
	public ResponseEntity<Response<?>> create(@RequestBody @Valid AppVersionCreateRequest request) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("appVersionId", appVersionService.create(request)))
			.build());
	}

	@GetMapping("/recent/{os-type}")
	public ResponseEntity<Response<AppVersionResponse>> getRecentByOsType(
		@PathVariable(name = "os-type") String osType) {
		return ResponseEntity.ok(Response.<AppVersionResponse>builder()
			.data(appVersionService.getRecentByOsType(osType))
			.build());
	}

	@GetMapping("/{app-id}")
	public ResponseEntity<AppVersionResponse> getById(@PathVariable(name = "app-id") Long appId) {

		return ResponseEntity.ok().body(appVersionService.getById(appId));
	}

	@PutMapping("/{app-id}")
	public ResponseEntity<Response<?>> updateById(
		@PathVariable(name = "app-id") Long appId,
		@RequestBody @Valid AppVersionUpdateRequest request
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("appVersionId", appVersionService.updateById(appId, request)))
			.build());
	}

	@DeleteMapping("/{app-id}")
	public ResponseEntity<Response<?>> deleteById(@PathVariable(name = "app-id") Long appId) {
		appVersionService.deleteById(appId);
		return ResponseEntity.ok().build();
	}
}

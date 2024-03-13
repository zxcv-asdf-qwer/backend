package co.kr.compig.api.presentation.app;

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
import co.kr.compig.api.presentation.app.request.AppVersionRequest;
import co.kr.compig.api.presentation.app.response.AppVersionResponse;
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
	public void create(@RequestBody @Valid AppVersionRequest request) {
		appVersionService.create(request);
	}

	@GetMapping("/recent/{os-type}")
	public ResponseEntity<AppVersionResponse> getRecentByOsType(@PathVariable(name = "os-type") String osType) {
		return ResponseEntity.ok().body(appVersionService.getRecentByOsType(osType));
	}

	@GetMapping("/{app-id}")
	public ResponseEntity<AppVersionResponse> getById(@PathVariable(name = "app-id") Long appId) {
		return ResponseEntity.ok().body(appVersionService.getById(appId));
	}

	@PutMapping("/{app-id}")
	public void updateById(
		@PathVariable(name = "app-id") Long appId,
		@RequestBody @Valid AppVersionRequest request
	) {
		appVersionService.updateById(appId, request);
	}

	@DeleteMapping("/{app-id}")
	public void deleteById(@PathVariable(name = "app-id") Long appId) {
		appVersionService.deleteById(appId);
	}
}

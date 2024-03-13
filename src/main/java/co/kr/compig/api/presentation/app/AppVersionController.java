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

	@GetMapping
	public ResponseEntity<AppVersionResponse> get() {
		return ResponseEntity.ok().body(appVersionService.get());
	}

	@GetMapping("/{version}")
	public ResponseEntity<AppVersionResponse> get(@PathVariable(name = "version") Integer version) {
		return ResponseEntity.ok().body(appVersionService.get(version));
	}

	@GetMapping("/names/{version}")
	public ResponseEntity<AppVersionResponse> get(@PathVariable(name = "version") String version) {
		return ResponseEntity.ok().body(appVersionService.get(version));
	}

	@PutMapping("/{version}")
	public void update(
		@PathVariable(name = "version") Integer version,
		@RequestBody @Valid AppVersionRequest request
	) {
		appVersionService.update(version, request);
	}

	@PutMapping("/names/{version}")
	public void update(
		@PathVariable(name = "version") String version,
		@RequestBody @Valid AppVersionRequest request
	) {
		appVersionService.update(version, request);
	}

	@DeleteMapping("/{version}")
	public void delete(@PathVariable(name = "version") Integer version) {
		appVersionService.delete(version);
	}

	@DeleteMapping("/names/{version}")
	public void delete(@PathVariable(name = "version") String version) {
		appVersionService.delete(version);
	}
}

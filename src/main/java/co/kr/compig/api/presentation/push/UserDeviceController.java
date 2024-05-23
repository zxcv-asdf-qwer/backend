package co.kr.compig.api.presentation.push;

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

import co.kr.compig.api.application.info.push.DeviceService;
import co.kr.compig.api.presentation.push.request.DeviceCreate;
import co.kr.compig.api.presentation.push.request.DeviceUpdate;
import co.kr.compig.api.presentation.push.response.DeviceResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "디바이스", description = "디바이스 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user/devices", produces = "application/json")
public class UserDeviceController {

	private final DeviceService deviceService;

	@Operation(summary = "생성")
	@PostMapping
	public ResponseEntity<Response<?>> create(@RequestBody @Valid DeviceCreate deviceCreate) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("deviceId", deviceService.save(deviceCreate)))
			.build());
	}

	@Operation(summary = "수정")
	@PutMapping("/{uuid}")
	public ResponseEntity<?> update(@PathVariable("uuid") String uuid, @RequestBody @Valid DeviceUpdate deviceUpdate) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("deviceId", deviceService.update(uuid, deviceUpdate)))
			.build());
	}

	@Operation(summary = "uuid 로 조회")
	@GetMapping("/{uuId}")
	public ResponseEntity<Response<DeviceResponse>> getByUuId(
		@PathVariable(name = "uuId") String uuId) {
		return ResponseEntity.ok(Response.<DeviceResponse>builder()
			.data(deviceService.detail(uuId).converterDto()).build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping("/{uuId}")
	public ResponseEntity<Response<?>> deleteByUuId(@PathVariable(name = "uuId") String uuId) {
		deviceService.removeDeviceUser(uuId);
		return ResponseEntity.ok().build();
	}

}

package co.kr.compig.api.presentation.push;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.info.push.PushService;
import co.kr.compig.api.presentation.push.request.PushCreate;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "푸시", description = "푸시 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/pushes", produces = "application/json")
public class PushController {

	private final PushService pushService;

	@Operation(summary = "생성")
	@PostMapping
	public ResponseEntity<Response<?>> create(@RequestBody @Valid PushCreate pushCreate) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("pushId", pushService.create(pushCreate)))
			.build());
	}
	//푸시 내역 조회

}

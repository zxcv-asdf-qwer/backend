package co.kr.compig.api.presentation.memo;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.memo.MemoService;
import co.kr.compig.api.presentation.memo.request.AdminMemoCreateRequest;
import co.kr.compig.api.presentation.memo.response.MemoResponse;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 간병공고 메모", description = "간병 공고 메모 관련 API")
@SecurityRequirement(name = "Bear Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/memo", produces = "application/json")
public class AdminMemoController {

	private final MemoService memoService;

	@Operation(summary = "생성하기")
	@PostMapping("/{orderId}")
	public ResponseEntity<Response<?>> createMemo(
		@PathVariable(name = "orderId") Long orderId,
		@RequestBody @Valid AdminMemoCreateRequest adminMemoCreateRequest
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("memoId", memoService.createMemo(orderId, adminMemoCreateRequest)))
			.build());
	}

	@Operation(summary = "조회")
	@GetMapping("/{orderId}")
	public ResponseEntity<List<MemoResponse>> getMemoList(
		@PathVariable(name = "orderId") Long orderId
	) {
		return ResponseEntity.ok(memoService.getMemoList(orderId));
	}
}

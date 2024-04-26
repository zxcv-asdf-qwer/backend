package co.kr.compig.api.presentation.board;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.kr.compig.api.application.board.BoardService;
import co.kr.compig.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "게시판 테스트", description = "게시판 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/board", produces = "application/json")
public class ImageTestController {
	private final BoardService boardService;

	@Operation(summary = "이미지 업로드")
	@PostMapping(path = "/imageUpload")
	public ResponseEntity<Response<?>> uploadImage(
		@RequestPart(value = "file", required = false) MultipartFile file) {
		return ResponseEntity.ok().body(Response.<String>builder()
			.data(boardService.uploadImage(file))
			.build());
	}
}

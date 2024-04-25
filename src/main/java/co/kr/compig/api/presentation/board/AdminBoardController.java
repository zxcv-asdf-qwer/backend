package co.kr.compig.api.presentation.board;

import java.util.List;
import java.util.Map;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.kr.compig.api.application.board.BoardService;
import co.kr.compig.api.presentation.board.request.BoardCreateRequest;
import co.kr.compig.api.presentation.board.request.BoardSearchRequest;
import co.kr.compig.api.presentation.board.request.BoardUpdateRequest;
import co.kr.compig.api.presentation.board.response.BoardDetailResponse;
import co.kr.compig.api.presentation.board.response.BoardResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 게시판", description = "게시판 관련 API")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/board", produces = "application/json")
public class AdminBoardController {

	private final BoardService boardService;

	@Operation(summary = "생성하기")
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Response<?>> createBoard(
		@RequestPart(value = "boardCreateRequest") @Valid BoardCreateRequest boardCreateRequest,
		@RequestPart(value = "file", required = false) List<MultipartFile> files) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("boardId", boardService.createBoard(boardCreateRequest, files)))
			.build());
	}

	@Operation(summary = "조회", description = "페이징")
	@GetMapping
	public ResponseEntity<PageResponse> getBoardPage(
		@ParameterObject @ModelAttribute BoardSearchRequest boardSearchRequest) {
		Page<BoardResponse> page = boardService.getBoardPage(boardSearchRequest);
		return PageResponse.ok(page.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{boardId}")
	public ResponseEntity<Response<BoardDetailResponse>> getBoard(
		@PathVariable(name = "boardId") Long boardId) {
		return ResponseEntity.ok(Response.<BoardDetailResponse>builder()
			.data(boardService.getBoard(boardId))
			.build());
	}

	@Operation(summary = "정보 수정하기")
	@PutMapping(path = "/{boardId}")
	public ResponseEntity<Response<?>> updateBoard(
		@PathVariable(name = "boardId") Long boardId,
		@RequestBody @Valid BoardUpdateRequest boardUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("boardId", boardService.updateBoard(boardId, boardUpdateRequest)))
			.build());
	}

	@Operation(summary = "삭제")
	@DeleteMapping(path = "/{boardId}")
	public ResponseEntity<Response<?>> deleteBoard(
		@PathVariable(name = "boardId") Long boardId) {
		boardService.deleteBoard(boardId);
		return ResponseEntity.ok().build();
	}

	/**
	 * base64로 인코딩 된 파일 받아 게시물 생성
	 */
	@Operation(summary = "생성하기 - base64")
	@PostMapping(path = "/base64")
	public ResponseEntity<Response<?>> createBoardBase64(
		@ParameterObject @Valid BoardCreateRequest boardCreateRequest,
		@RequestPart(value = "file", required = false) Map<String, String> files
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("boardId", boardService.createBoardBaseFile(boardCreateRequest, files)))
			.build());
	}

	/**
	 * s3 파일 base64로 인코딩하여 보여주기
	 */
	@Operation(summary = "상세 조회 - base64")
	@GetMapping(path = "/base64/{boardId}")
	public ResponseEntity<Response<BoardDetailResponse>> getBoardBase(
		@PathVariable(name = "boardId") Long boardId) {
		return ResponseEntity.ok(Response.<BoardDetailResponse>builder()
			.data(boardService.getBoard(boardId))
			.build());
	}

}

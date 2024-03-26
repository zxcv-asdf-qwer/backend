package co.kr.compig.api.presentation.board;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import co.kr.compig.api.application.board.BoardService;
import co.kr.compig.api.presentation.board.request.BoardCreateRequest;
import co.kr.compig.api.presentation.board.request.BoardSearchRequest;
import co.kr.compig.api.presentation.board.request.BoardUpdateRequest;
import co.kr.compig.api.presentation.board.response.BoardDetailResponse;
import co.kr.compig.api.presentation.board.response.BoardResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pv/board", produces = "application/json")
public class AdminBoardController {

	private final BoardService boardService;

	@PostMapping
	public ResponseEntity<Response<?>> createBoard(
		@ModelAttribute @Valid BoardCreateRequest boardCreateRequest,
		MultipartHttpServletRequest multipartRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("boardId", boardService.createBoard(boardCreateRequest, multipartRequest)))
			.build());
	}

	@GetMapping
	public ResponseEntity<PageResponse<BoardResponse>> pageListBoard(
		@ModelAttribute @Valid BoardSearchRequest boardSearchRequest, Pageable pageable) {
		Page<BoardResponse> page = boardService.pageListBoard(boardSearchRequest, pageable);
		PageResponse<BoardResponse> pageResponse = new PageResponse<>(page.getContent(), pageable,
			page.getTotalElements());
		return ResponseEntity.ok(pageResponse);
	}

	@GetMapping(path = "/{boardId}")
	public ResponseEntity<Response<BoardDetailResponse>> getBoard(
		@PathVariable(name = "boardId") Long boardId) {
		return ResponseEntity.ok(Response.<BoardDetailResponse>builder()
			.data(boardService.getBoard(boardId))
			.build());
	}

	@PutMapping(path = "/{boardId}")
	public ResponseEntity<Response<?>> updateBoard(@PathVariable(name = "boardId") Long boardId,
		@RequestBody @Valid BoardUpdateRequest boardUpdateRequest) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("boardId", boardService.updateBoard(boardId, boardUpdateRequest)))
			.build());
	}

	@DeleteMapping(path = "/{boardId}")
	public ResponseEntity<Response<?>> deleteBoard(@PathVariable(name = "boardId") Long boardId) {
		boardService.deleteBoard(boardId);
		return ResponseEntity.ok().build();
	}

	/**
	 * base64로 인코딩 된 파일 받아 게시물 생성
	 */
	@PostMapping(path = "/base64")
	public ResponseEntity<Response<?>> createBoardBase64(
		@ModelAttribute @Valid BoardCreateRequest boardCreateRequest,
		@RequestPart(value = "file", required = false) Map<String, String> files
	) {
		return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
			.data(Map.of("boardId", boardService.createBoardBaseFile(boardCreateRequest, files)))
			.build());
	}

	/**
	 * s3 파일 base64로 인코딩하여 보여주기
	 */
	@GetMapping(path = "/base64/{boardId}")
	public ResponseEntity<Response<BoardDetailResponse>> getBoardBase(
		@PathVariable(name = "boardId") Long boardId) {
		return ResponseEntity.ok(Response.<BoardDetailResponse>builder()
			.data(boardService.getBoard(boardId))
			.build());
	}

}
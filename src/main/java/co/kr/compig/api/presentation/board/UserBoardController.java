package co.kr.compig.api.presentation.board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.application.board.BoardService;
import co.kr.compig.api.presentation.board.request.BoardSearchRequest;
import co.kr.compig.api.presentation.board.response.BoardDetailResponse;
import co.kr.compig.api.presentation.board.response.BoardResponse;
import co.kr.compig.global.dto.Response;
import co.kr.compig.global.dto.pagination.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/pb/board", produces = "application/json")
public class UserBoardController {
	private final BoardService boardService;

	@GetMapping
	public ResponseEntity<SliceResponse<BoardResponse>> pageListBoard(
		@ModelAttribute @Valid BoardSearchRequest boardSearchRequest, Pageable pageable) {
		Slice<BoardResponse> slice = boardService.pageListBoardCursor(boardSearchRequest, pageable);
		SliceResponse<BoardResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable, slice.hasNext());
		return ResponseEntity.ok(sliceResponse);
	}

	@GetMapping(path = "/{boardId}")
	public ResponseEntity<Response<BoardDetailResponse>> getBoard(@PathVariable(name = "boardId") Long boardId) {
		return ResponseEntity.ok(Response.<BoardDetailResponse>builder()
			.data(boardService.getBoard(boardId))
			.build());
	}
}

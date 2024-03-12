package co.kr.compig.api.board.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.kr.compig.api.board.dto.BoardDetailResponse;
import co.kr.compig.api.board.dto.BoardResponse;
import co.kr.compig.api.board.dto.BoardSearchRequest;
import co.kr.compig.common.dto.Response;
import co.kr.compig.common.dto.pagination.SliceResponse;
import co.kr.compig.service.board.BoardService;
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
		@RequestBody @Valid BoardSearchRequest boardSearchRequest, Pageable pageable) {
		Slice<BoardResponse> slice = boardService.pageListBoardCursor(boardSearchRequest, pageable);
		SliceResponse<BoardResponse> sliceResponse = new SliceResponse<>(slice.getContent(), pageable, slice.hasNext());
		return ResponseEntity.ok(sliceResponse);
	}

	@GetMapping("/{boardId}")
	public ResponseEntity<Response<BoardDetailResponse>> getBoard(@PathVariable(name = "boardId") Long boardId) {
		return ResponseEntity.ok(Response.<BoardDetailResponse>builder()
			.data(boardService.getBoard(boardId))
			.build());
	}
}

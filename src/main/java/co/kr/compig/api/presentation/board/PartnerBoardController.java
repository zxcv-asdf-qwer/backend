package co.kr.compig.api.presentation.board;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "간병인 게시판", description = "게시판 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/partner/board", produces = "application/json")
public class PartnerBoardController {
	private final BoardService boardService;

	@Operation(summary = "조회")
	@GetMapping
	public ResponseEntity<SliceResponse<BoardResponse>> getBoardSlice(
		@ParameterObject @ModelAttribute @Valid BoardSearchRequest boardSearchRequest) {
		return ResponseEntity.ok(boardService.getBoardSlice(boardSearchRequest));
	}

	@Operation(summary = "상세 조회")
	@GetMapping(path = "/{boardId}")
	public ResponseEntity<Response<BoardDetailResponse>> getBoard(
		@PathVariable(name = "boardId") Long boardId) {
		return ResponseEntity.ok(Response.<BoardDetailResponse>builder()
			.data(boardService.getBoard(boardId))
			.build());
	}
}

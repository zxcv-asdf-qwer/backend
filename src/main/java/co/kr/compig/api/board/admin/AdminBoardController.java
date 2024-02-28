package co.kr.compig.api.board.admin;

import co.kr.compig.api.board.dto.BoardCreateRequest;
import co.kr.compig.api.board.dto.BoardResponse;
import co.kr.compig.api.board.dto.BoardSearchRequest;
import co.kr.compig.api.board.dto.BoardUpdateRequest;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.board.BoardService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public ResponseEntity<Response<Page<BoardResponse>>> pageListBoard(
      @RequestBody @Valid BoardSearchRequest boardSearchRequest, Pageable pageable) {
    return ResponseEntity.ok().body(Response.<Page<BoardResponse>>builder()
        .data(boardService.pageListBoard(boardSearchRequest, pageable))
        .build());
  }

  @GetMapping("/{boardId}")
  public ResponseEntity<Response<BoardResponse>> getBoard(
      @PathVariable(name = "boardId") Long boardId) {
    return ResponseEntity.ok(Response.<BoardResponse>builder()
        .data(boardService.getBoard(boardId))
        .build());
  }

  @PutMapping("/{boardId}")
  public ResponseEntity<Response<?>> updateBoard(@PathVariable(name = "boardId") Long boardId,
      @RequestBody @Valid BoardUpdateRequest boardUpdateRequest) {
    return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
        .data(Map.of("boardId", boardService.updateBoard(boardId, boardUpdateRequest)))
        .build());
  }

  @DeleteMapping(path = "/{boardId}")
  public ResponseEntity<Response<?>> deleteBoard(@PathVariable(name = "boardId") Long boardId) {
    return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
        .data(Map.of("boardId", boardService.deleteBoard(boardId)))
        .build());
  }

  //////////////////////////////////////////////////
  // base64
  @PostMapping(path = "/base")
  public ResponseEntity<Response<?>> createBoardBase64(
      @ModelAttribute @Valid BoardCreateRequest boardCreateRequest,
      @RequestPart(value = "file") Map<String, String> file
  ){
    return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
        .data(Map.of("boardId", boardService.createBoardBaseFile(boardCreateRequest, file)))
        .build());
  }

}

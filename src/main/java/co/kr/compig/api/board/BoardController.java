package co.kr.compig.api.board;

import co.kr.compig.api.board.dto.BoardCreateRequest;
import co.kr.compig.api.board.dto.BoardResponse;
import co.kr.compig.api.board.dto.BoardSearchRequest;
import co.kr.compig.api.board.dto.BoardUpdateRequest;
import co.kr.compig.common.dto.Response;
import co.kr.compig.service.board.BoardService;
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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/board", produces = "application/json")
public class BoardController {
  private final BoardService boardService;


  @GetMapping
  public ResponseEntity<Response<Page<BoardResponse>>> pageListBoard(@ModelAttribute BoardSearchRequest boardSearchRequest, Pageable pageable) {
    return ResponseEntity.ok().body(Response.<Page<BoardResponse>>builder()
        .data(boardService.pageListQuestion(boardSearchRequest, pageable))
        .build());
  }

  @PostMapping
  public ResponseEntity<Response<?>> createBoard(BoardCreateRequest boardCreateRequest){
    return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
        .data(Map.of("boardId", boardService.createBoard(boardCreateRequest)))
        .build());
  }

  @PutMapping("/{boardId}")
  public ResponseEntity<Response<?>> updateBoard(@PathVariable(name = "boardId") Long boardId, @RequestBody BoardUpdateRequest boardUpdateRequest){
    return ResponseEntity.ok().body(Response.<Map<String, Long>>builder()
        .data(Map.of("boardId", boardService.updateBoard(boardId, boardUpdateRequest)))
        .build());
  }

  @DeleteMapping(path = "/{boardId}")
  public ResponseEntity<Response<?>> deleteBoard(@PathVariable(name = "boardId") Long boardId) {
    boardService.deleteBoard(boardId);
    return ResponseEntity.noContent()
        .build();
  }
}

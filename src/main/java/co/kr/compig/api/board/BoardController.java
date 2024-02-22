package co.kr.compig.api.board;

import co.kr.compig.api.board.dto.BoardCreate;
import co.kr.compig.api.board.dto.BoardResponseDto;
import co.kr.compig.api.board.dto.BoardUpdate;
import co.kr.compig.common.dto.Response;
import co.kr.compig.domain.board.Board;
import co.kr.compig.domain.member.Member;
import co.kr.compig.service.board.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping()
  public List<Board> getBoardList(){
    return boardService.getBoardList();
  }

  @GetMapping("/{boardId}")
  public BoardResponseDto getSingleBoard(@PathVariable Long boardId){
    return boardService.getSingleList(boardId);
  }

  @PostMapping
  public Response<?> createBoard(BoardCreate boardCreate, Member member){
    return boardService.createBoard(boardCreate, member);
  }

  @PutMapping("/{boardId}")
  public Response<?> updateBoard(@PathVariable Long boardId, @RequestBody BoardUpdate boardUpdate){
    return boardService.updateBoard(boardId, boardUpdate);
  }

  @DeleteMapping("/{boardId}")
  public Response<?> deleteBoard(@PathVariable  Long boardId){
    return boardService.deleteBoard(boardId);
  }
}

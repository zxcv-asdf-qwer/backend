package co.kr.compig.service.board;

import static co.kr.compig.common.exception.dto.ErrorCode.*;

import co.kr.compig.api.board.dto.BoardCreate;
import co.kr.compig.api.board.dto.BoardResponseDto;
import co.kr.compig.api.board.dto.BoardUpdate;
import co.kr.compig.common.dto.Response;
import co.kr.compig.common.exception.NotExistDataException;
import co.kr.compig.domain.board.Board;
import co.kr.compig.domain.board.BoardRepository;
import co.kr.compig.domain.member.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
  private final BoardRepository boardRepository;

  public List<Board> getBoardList(){
    return boardRepository.findAll();
  }

  public BoardResponseDto getSingleList(Long boardId) {
    Board board = findBoard(boardId);
    return new BoardResponseDto(board);
  }


  public Response<?> createBoard(BoardCreate boardCreate, Member member) {
    boardRepository.save(new Board(boardCreate, member));
    return new Response<>("등록이 완료되었습니다.");
  }

  public Response<?> deleteBoard(Long boardId) {
    Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("없는 게시물입니다."));
    boardRepository.delete(board);
    return new Response<>("삭제가 완료되었습니다.");
  }

  public Response<?> updateBoard(Long boardId, BoardUpdate boardUpdate) {
    Board board = findBoard(boardId);
    board.update(boardUpdate);
    return new Response<>("수정이 완료되었습니다.");
  }

  private Board findBoard(Long boardId){
    return boardRepository.findById(boardId).orElseThrow(() ->
        new NotExistDataException(POST_NOT_EXIST));
  }
}

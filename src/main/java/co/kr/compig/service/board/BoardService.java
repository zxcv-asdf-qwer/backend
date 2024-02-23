package co.kr.compig.service.board;

import co.kr.compig.api.board.dto.BoardCreateRequest;
import co.kr.compig.api.board.dto.BoardResponse;
import co.kr.compig.api.board.dto.BoardSearchRequest;
import co.kr.compig.api.board.dto.BoardUpdateRequest;
import co.kr.compig.common.exception.NotExistDataException;
import co.kr.compig.domain.board.Board;
import co.kr.compig.domain.board.BoardRepository;
import co.kr.compig.domain.board.BoardRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

  private final BoardRepository boardRepository;
  private final BoardRepositoryCustom boardRepositoryCustom;


  public Long createBoard(BoardCreateRequest boardCreateRequest) {
    Board board = boardCreateRequest.converterEntity();
    return boardRepository.save(board).getId();
  }

  public Page<BoardResponse> pageListBoard(BoardSearchRequest boardSearchRequest,
      Pageable pageable) {
    return boardRepositoryCustom.findPage(boardSearchRequest, pageable);
  }

  @Transactional
  public Long updateBoard(Long boardId, BoardUpdateRequest boardUpdateRequest) {
    Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
    board.update(boardUpdateRequest);
    return board.getId();
  }

  public void deleteBoard(Long boardId) {
    Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
    boardRepository.delete(board);
  }

  public BoardResponse getBoard(Long boardId) {
    Board board = boardRepository.findById(boardId).orElseThrow(NotExistDataException::new);
    board.increaseViewCount();
    return new BoardResponse(board);
  }
}

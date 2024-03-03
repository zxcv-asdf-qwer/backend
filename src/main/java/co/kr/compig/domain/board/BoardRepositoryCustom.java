package co.kr.compig.domain.board;

import co.kr.compig.api.board.dto.BoardResponse;
import co.kr.compig.api.board.dto.BoardSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepositoryCustom {

  //TODO pb에 Slice pv에 Page
  Page<BoardResponse> findPage(BoardSearchRequest boardSearchRequest, Pageable pageable);

  Slice<BoardResponse> findAllByCondition(Long cursorId, BoardSearchRequest boardSearchRequest, Pageable pageable);
}

package co.kr.compig.domain.board;

import co.kr.compig.api.board.dto.BoardResponse;
import co.kr.compig.api.board.dto.BoardSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

public interface BoardRepositoryCustom {

  Page<BoardResponse> findPage(BoardSearchRequest boardSearchRequest, Pageable pageable);
}

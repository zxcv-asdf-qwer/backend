package co.kr.compig.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.board.dto.BoardResponse;
import co.kr.compig.api.board.dto.BoardSearchRequest;

@Repository
public interface BoardRepositoryCustom {

	//TODO pb에 Slice pv에 Page
	Page<BoardResponse> findPage(BoardSearchRequest boardSearchRequest, Pageable pageable);

	Slice<BoardResponse> findAllByCondition(BoardSearchRequest boardSearchRequest, Pageable pageable);
}

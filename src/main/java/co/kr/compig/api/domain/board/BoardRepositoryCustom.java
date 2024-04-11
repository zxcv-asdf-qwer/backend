package co.kr.compig.api.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.board.request.BoardSearchRequest;
import co.kr.compig.api.presentation.board.response.BoardResponse;

@Repository
public interface BoardRepositoryCustom {

	//TODO pb에 Slice pv에 Page
	Page<BoardResponse> getBoardPage(BoardSearchRequest boardSearchRequest, Pageable pageable);

	Slice<BoardResponse> findAllByCondition(BoardSearchRequest boardSearchRequest, Pageable pageable);
}

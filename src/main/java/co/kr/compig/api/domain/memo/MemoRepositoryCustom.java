package co.kr.compig.api.domain.memo;

import java.util.List;

import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.memo.response.MemoResponse;

@Repository
public interface MemoRepositoryCustom {
	List<MemoResponse> getMemoList(Long orderId);

}

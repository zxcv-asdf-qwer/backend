package co.kr.compig.api.domain.memo;

import static co.kr.compig.api.domain.memo.QMemo.*;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.api.presentation.memo.response.MemoResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemoRepositoryImpl implements MemoRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<MemoResponse> getMemoList(Long orderId) {
		JPAQuery<Memo> query = jpaQueryFactory
			.from(memo)
			.where(memo.careOrder.id.eq(orderId))
			.select(memo);

		List<Memo> memos = query.fetch();

		List<MemoResponse> responses = memos.stream()
			.map(Memo::toMemoResponse)
			.collect(Collectors.toList());

		return responses;
	}

}

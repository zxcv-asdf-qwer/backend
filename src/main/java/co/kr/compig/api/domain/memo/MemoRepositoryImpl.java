package co.kr.compig.api.domain.memo;

import static co.kr.compig.api.domain.memo.QMemo.*;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.api.presentation.memo.response.MemoResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemoRepositoryImpl implements MemoRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<MemoResponse> getMemoList(Long orderId) {
		JPAQuery<MemoResponse> query = jpaQueryFactory
			.from(memo)
			.where(memo.careOrder.id.eq(orderId))
			.select(Projections.constructor(MemoResponse.class,
				memo.id,
				memo.contents,
				memo.createdAndModified.createdBy,
				memo.createdAndModified.createdOn
			))
			.orderBy(memo.createdAndModified.createdOn.desc());
		return query.fetch();
	}

	@Override
	public Long getMemoCount(Long orderId) {
		JPAQuery<Long> query = jpaQueryFactory
			.select(memo.count())
			.from(memo)
			.where(memo.careOrder.id.eq(orderId));
		return query.fetchOne();
	}
}

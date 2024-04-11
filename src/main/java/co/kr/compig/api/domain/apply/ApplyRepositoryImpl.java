package co.kr.compig.api.domain.apply;

import static co.kr.compig.api.domain.apply.QApply.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import com.google.common.base.CaseFormat;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.api.presentation.apply.request.ApplySearchRequest;
import co.kr.compig.api.presentation.apply.response.ApplyResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<ApplyResponse> getApplyPage(ApplySearchRequest applySearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(applySearchRequest);

		JPAQuery<ApplyResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(ApplyResponse.class,
					apply.id,
					apply.member.id,
					apply.careOrder.id,
					apply.applyStatusCode
				)
			);

		applySorting(query, pageable);

		List<ApplyResponse> applys = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(apply.count());

		return PageableExecutionUtils.getPage(applys, pageable, countQuery::fetchOne);
	}

	@Override
	public Slice<ApplyResponse> findAllByCondition(ApplySearchRequest applySearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(applySearchRequest);

		JPAQuery<ApplyResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(ApplyResponse.class,
					apply.id,
					apply.member.id,
					apply.careOrder.id,
					apply.applyStatusCode
				)
			);

		applySorting(query, pageable);

		List<ApplyResponse> applies = query
			.where(cursorCursorId(Long.valueOf(applySearchRequest.getCursorId())))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = false;
		if (applies.size() > pageable.getPageSize()) {
			applies.remove(pageable.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(applies, pageable, hasNext);
	}

	private BooleanExpression createPredicate(ApplySearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

		if (request.getMemberId() != null) {
			predicate = predicate.and(apply.member.id.eq(request.getMemberId()));
		}
		if (request.getCareOrderId() != null) {
			predicate = predicate.and(apply.careOrder.id.eq(request.getCareOrderId()));
		}
		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(apply)
			.where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, apply,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

	private BooleanExpression cursorCursorId(Long cursorId) {
		if (cursorId == null)
			return null;
		return apply.id.lt(cursorId);
	}
}

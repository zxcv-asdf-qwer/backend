package co.kr.compig.api.domain.apply;

import static co.kr.compig.api.domain.apply.QApply.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

import co.kr.compig.api.presentation.apply.response.ApplyResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<ApplyResponse> findPage(Pageable pageable) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

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
	public Slice<ApplyResponse> findAllByCondition(Pageable pageable) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

		JPAQuery<ApplyResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(ApplyResponse.class,
					apply.id,
					apply.member.id,
					apply.careOrder.id,
					apply.applyStatusCode
				)
			);

		applySorting(query, pageable);

		return null;
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
}

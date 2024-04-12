package co.kr.compig.api.domain.settle;

import static co.kr.compig.api.domain.settle.QSettle.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import co.kr.compig.api.presentation.settle.request.SettleSearchRequest;
import co.kr.compig.api.presentation.settle.response.SettleResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SettleRepositoryImpl implements SettleRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<SettleResponse> getPage(SettleSearchRequest request, Pageable pageable) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<SettleResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(SettleResponse.class,
					settle.id,
					settle.guardianFees,
					settle.partnerFees,
					settle.useYn
				)
			);

		//정렬
		applySorting(query, pageable);

		List<SettleResponse> responses = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) //페이징
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(settle.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(settle)
			.where(predicate);
	}

	private BooleanExpression createPredicate(SettleSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

		if (request.getToCreatedOn() != null) {
			predicate = predicate.and(
				settle.createdAndModified.createdOn.loe(request.getToCreatedOn()));
		}
		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, settle,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}
}

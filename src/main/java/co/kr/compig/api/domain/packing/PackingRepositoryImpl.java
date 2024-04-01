package co.kr.compig.api.domain.packing;

import static co.kr.compig.api.domain.packing.QPacking.*;

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

import co.kr.compig.api.presentation.packing.request.PackingSearchRequest;
import co.kr.compig.api.presentation.packing.response.PackingResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PackingRepositoryImpl implements PackingRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<PackingResponse> findPage(PackingSearchRequest packingSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(packingSearchRequest);

		JPAQuery<PackingResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(PackingResponse.class,
				packing.id,
				packing.careOrder.id,
				packing.settleGroup.id
			));

		applySorting(query, pageable);

		List<PackingResponse> packings = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(packing.count());

		return PageableExecutionUtils.getPage(packings, pageable, countQuery::fetchOne);
	}

	private BooleanExpression createPredicate(PackingSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getCareOrderId() != null) {
			predicate = predicate.and(packing.careOrder.id.eq(request.getCareOrderId()));
		}
		if (request.getSettleGroupId() != null) {
			predicate = predicate.and(packing.settleGroup.id.eq(request.getSettleGroupId()));
		}
		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(packing)
			.where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, packing,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target
			);
			query.orderBy(orderSpecifier);
		}
	}
}
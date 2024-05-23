package co.kr.compig.api.domain.packing;

import static co.kr.compig.api.domain.order.QCareOrder.*;
import static co.kr.compig.api.domain.packing.QPacking.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.google.common.base.CaseFormat;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.api.presentation.payment.request.PaymentExchangeOneDaySearchRequest;
import co.kr.compig.global.code.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PackingRepositoryImpl implements PackingRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, packing,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target
			);
			query.orderBy(orderSpecifier);
		}
	}

	@Override
	public Page<Packing> getExchangeOneDayPage(PaymentExchangeOneDaySearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Packing> query = jpaQueryFactory
			.select(packing)
			.from(packing)
			.leftJoin(packing.careOrder, careOrder)
			.where(
				packing.endDateTime.loe(LocalDateTime.now()),
				careOrder.orderStatus.in(OrderStatus.MATCHING_WAITING, OrderStatus.MATCHING_COMPLETE,
					OrderStatus.ORDER_COMPLETE)
			)
			.where(predicate) // 추가적인 필터 조건
			.orderBy(packing.id.desc());  // 지갑 ID에 따라 내림차순 정렬

		Pageable pageable = request.pageable();

		//정렬
		applySorting(query, pageable);

		List<Packing> packages = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(packing.count())
			.leftJoin(packing.careOrder, careOrder)
			.where(
				packing.endDateTime.loe(LocalDateTime.now()),
				careOrder.orderStatus.in(OrderStatus.MATCHING_WAITING, OrderStatus.MATCHING_COMPLETE,
					OrderStatus.ORDER_COMPLETE)
			)
			.where(predicate); // 추가적인 필터 조건

		return PageableExecutionUtils.getPage(packages, pageable, countQuery::fetchOne);
	}

	private BooleanExpression createPredicate(PaymentExchangeOneDaySearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getCareOrderId() != null) {
			predicate = predicate.and(packing.careOrder.id.eq(request.getCareOrderId()));
		}

		return predicate;
	}
}

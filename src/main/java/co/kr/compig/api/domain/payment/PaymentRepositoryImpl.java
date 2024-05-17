package co.kr.compig.api.domain.payment;

import static co.kr.compig.api.domain.payment.QPayment.*;

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

import co.kr.compig.api.presentation.payment.request.PaymentSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<PaymentResponse> findPage(PaymentSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);
		JPAQuery<PaymentResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(PaymentResponse.class,
				payment.id,
				payment.careOrder.id
			));
		Pageable pageable = request.pageable();

		applySorting(query, pageable);

		List<PaymentResponse> payments = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(payment.count());

		return PageableExecutionUtils.getPage(payments, pageable, countQuery::fetchOne);
	}

	@Override
	public Slice<PaymentResponse> getPaymentSlice(PaymentSearchRequest paymentSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(paymentSearchRequest);
		JPAQuery<PaymentResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(PaymentResponse.class,
				payment.id,
				payment.careOrder.id
			));

		applySorting(query, pageable);

		List<PaymentResponse> payments = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(payment.count());

		return PageableExecutionUtils.getPage(payments, pageable, countQuery::fetchOne);
	}

	private BooleanExpression createPredicate(PaymentSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getPackingId() != null) {
			predicate = predicate.and(payment.careOrder.id.eq(request.getPackingId()));
		}
		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(payment)
			.where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, payment,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

}

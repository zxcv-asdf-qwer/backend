package co.kr.compig.api.domain.payment;

import static co.kr.compig.api.domain.payment.QPaymentCancel.*;

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

import co.kr.compig.api.presentation.payment.request.PaymentCancelSearchRequest;
import co.kr.compig.api.presentation.payment.response.PaymentCancelResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentCancelRepositoryImpl implements PaymentCancelRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<PaymentCancelResponse> findPage(PaymentCancelSearchRequest paymentCancelSearchRequest,
		Pageable pageable) {
		BooleanExpression predicate = createPredicate(paymentCancelSearchRequest);

		JPAQuery<PaymentCancelResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(PaymentCancelResponse.class,
				paymentCancel.id,
				paymentCancel.payment.id));

		applySorting(query, pageable);

		List<PaymentCancelResponse> paymentCancels = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(paymentCancel.count());

		return PageableExecutionUtils.getPage(paymentCancels, pageable, countQuery::fetchOne);
	}

	private BooleanExpression createPredicate(PaymentCancelSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getPaymentId() != null) {
			predicate = predicate.and(paymentCancel.payment.id.eq(request.getPaymentId()));
		}
		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(paymentCancel)
			.where(predicate);
	}

	private void applySorting(JPAQuery<?> query, org.springframework.data.domain.Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, paymentCancel,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}
}

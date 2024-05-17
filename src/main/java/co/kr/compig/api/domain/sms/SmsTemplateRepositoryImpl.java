package co.kr.compig.api.domain.sms;

import static co.kr.compig.api.domain.sms.QSmsTemplate.*;

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

import co.kr.compig.api.presentation.sms.request.SmsTemplateSearchRequest;
import co.kr.compig.api.presentation.sms.response.SmsTemplateResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SmsTemplateRepositoryImpl implements SmsTemplateRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<SmsTemplateResponse> findPage(SmsTemplateSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<SmsTemplateResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(SmsTemplateResponse.class
				)
			);
		Pageable pageable = request.pageable();

		//정렬
		applySorting(query, pageable);

		List<SmsTemplateResponse> responses = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) //페이징
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(smsTemplate.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(smsTemplate)
			.where(predicate);
	}

	private BooleanExpression createPredicate(SmsTemplateSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getKeyword() != null) {
			predicate = predicate.and(smsTemplate.contents.eq(request.getKeyword()));
		}

		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, smsTemplate,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

}

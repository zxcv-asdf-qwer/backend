package co.kr.compig.api.domain.sms;

import static co.kr.compig.api.domain.sms.QSms.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import com.google.common.base.CaseFormat;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.api.presentation.sms.request.SmsSearchRequest;
import co.kr.compig.api.presentation.sms.response.SmsResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SmsRepositoryImpl implements SmsRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<SmsResponse> findPage(SmsSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Sms> query = createBaseQuery(predicate)
			.select(sms);
		Pageable pageable = request.pageable();

		//정렬
		applySorting(query, pageable);

		List<Sms> smsList = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) //페이징
			.fetch();

		List<SmsResponse> responses = smsList.stream()
			.map(Sms::toSmsResponse)
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(sms.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(sms)
			.where(predicate);
	}

	private BooleanExpression createPredicate(SmsSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getKeyword() != null) {
			predicate = predicate.and(sms.contents.eq(request.getKeyword()));
		}

		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, sms,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

}

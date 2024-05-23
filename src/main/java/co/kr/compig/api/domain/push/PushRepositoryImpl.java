package co.kr.compig.api.domain.push;

import static co.kr.compig.api.domain.push.QDevice.*;
import static co.kr.compig.api.domain.push.QPush.*;

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

import co.kr.compig.api.presentation.push.request.PushSearchRequest;
import co.kr.compig.api.presentation.push.response.PushResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PushRepositoryImpl implements PushRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<PushResponse> findPage(PushSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Push> query = createBaseQuery(predicate)
			.select(push);
		Pageable pageable = request.pageable();

		//정렬
		applySorting(query, pageable);

		List<Push> pushes = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) //페이징
			.fetch();

		List<PushResponse> responses = pushes.stream()
			.map(push -> {
				Device deviceByUUid = jpaQueryFactory
					.selectFrom(device)
					.where(device.deviceUuid.eq(push.getDeviceUuid()))
					.fetchOne();
				return push.toPushResponse(deviceByUUid != null ? deviceByUUid.getMember() : null);
			})
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(push.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(push)
			.where(predicate);
	}

	private BooleanExpression createPredicate(PushSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getKeyword() != null) {
			predicate = predicate.and(push.message.eq(request.getKeyword()));
		}

		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, push,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

}

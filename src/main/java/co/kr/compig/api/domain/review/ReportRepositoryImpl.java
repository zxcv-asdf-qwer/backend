package co.kr.compig.api.domain.review;

import static co.kr.compig.api.domain.review.QReport.*;

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

import co.kr.compig.api.presentation.review.request.ReportSearchRequest;
import co.kr.compig.api.presentation.review.response.ReportResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<ReportResponse> getReportPage(ReportSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Report> query = createBaseQuery(predicate)
			.select(report);
		Pageable pageable = request.pageable();

		applySorting(query, pageable);

		List<Report> reports = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<ReportResponse> responses = reports.stream()
			.map(Report::toResponse)
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(report.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(report)
			.where(predicate);
	}

	private BooleanExpression createPredicate(ReportSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, report,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target
			);
			query.orderBy(orderSpecifier);
		}
	}
}

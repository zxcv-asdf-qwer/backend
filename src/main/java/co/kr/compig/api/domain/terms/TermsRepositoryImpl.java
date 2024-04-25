package co.kr.compig.api.domain.terms;

import static co.kr.compig.api.domain.terms.QTerms.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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

import co.kr.compig.api.presentation.terms.request.TermsSearchRequest;
import co.kr.compig.api.presentation.terms.response.TermsListResponse;
import co.kr.compig.api.presentation.terms.response.TermsResponse;
import co.kr.compig.global.code.TermsType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<TermsResponse> getTermsPage(TermsSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Terms> query = createBaseQuery(predicate)
			.select(terms)
			.orderBy(terms.createdAndModified.createdOn.desc());

		Pageable pageable = request.pageable();

		applySorting(query, pageable);

		List<Terms> termsList = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<TermsResponse> responses = termsList.stream()
			.map(Terms::toResponse)
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(terms.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	@Override
	public Slice<TermsResponse> getTermsSlice(TermsSearchRequest termsSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(termsSearchRequest);

		JPAQuery<TermsResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(TermsResponse.class,
				terms.id,
				terms.termsType,
				terms.createdAndModified.createdBy.userNm,
				terms.createdAndModified.createdOn
			));

		applySorting(query, pageable);

		List<TermsResponse> responses = query
			.where(cursorCursorId(Long.valueOf(termsSearchRequest.getCursorId())))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = false;
		if (responses.size() > pageable.getPageSize()) {
			responses.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(responses, pageable, hasNext);
	}

	@Override
	public Map<TermsType, List<TermsListResponse>> getTermsList() {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

		JPAQuery<Terms> query = createBaseQuery(predicate)
			.select(terms)
			.orderBy(terms.createdAndModified.createdOn.desc());

		return query.fetch().stream()
			.collect(Collectors.groupingBy(Terms::getTermsType,
				Collectors.mapping(Terms::toTermListResponse, Collectors.toList())));
	}

	private BooleanExpression cursorCursorId(Long cursorId) {
		if (cursorId == null)
			return null;
		return terms.id.lt(cursorId);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(terms)
			.where(predicate);
	}

	private BooleanExpression createPredicate(TermsSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request == null) {
			return predicate;
		}
		if (request.getTermsType() != null) {
			predicate = predicate.and(terms.termsType.eq(request.getTermsType()));
		}

		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, terms,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target
			);
			query.orderBy(orderSpecifier);
		}
	}
}

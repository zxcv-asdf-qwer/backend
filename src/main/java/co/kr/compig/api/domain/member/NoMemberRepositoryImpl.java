package co.kr.compig.api.domain.member;

import static co.kr.compig.api.domain.member.QNoMember.*;

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

import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.response.NoMemberResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NoMemberRepositoryImpl implements NoMemberRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<NoMemberResponse> getNoMemberPage(MemberSearchRequest request, Pageable pageable) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<NoMember> query = jpaQueryFactory
			.selectFrom(noMember);

		//정렬
		applySorting(query, pageable);

		List<NoMember> members = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) // 페이징
			.fetch();

		List<NoMemberResponse> responses = members.stream()
			.map(NoMember::toNoMemberResponse)
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(noMember.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(noMember)
			.where(predicate);
	}

	private BooleanExpression createPredicate(MemberSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request == null) {
			return predicate;
		}
		if (request.getKeyword() != null) {
			predicate = predicate.and(noMember.userNm.eq(request.getKeyword()));
		}

		if (request.getFromCreatedOn() != null) {
			predicate = predicate.and(
				noMember.createdAndModified.createdOn.goe(request.getFromCreatedOn())); //크거나 같고(.goe)
		}

		if (request.getToCreatedOn() != null) {
			predicate = predicate.and(
				noMember.createdAndModified.createdOn.loe(request.getToCreatedOn())); //작거나 같은(.loe)
		}

		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, noMember,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

}

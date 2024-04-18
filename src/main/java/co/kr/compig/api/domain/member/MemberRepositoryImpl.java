package co.kr.compig.api.domain.member;

import static co.kr.compig.api.domain.member.QMember.*;

import java.util.List;
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

import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.response.MemberPageResponse;
import co.kr.compig.api.presentation.member.response.MemberResponse;
import co.kr.compig.api.presentation.member.response.PartnerMemberResponse;
import co.kr.compig.global.code.UserType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Slice<MemberPageResponse> getUserPageCursor(MemberSearchRequest request, Pageable pageable) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<MemberPageResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(MemberPageResponse.class,
					member.id,
					member.userId,
					member.userNm,
					member.email,
					member.telNo,
					member.gender,
					member.useYn,
					member.userType,
					member.memberRegisterType
				)
			);

		//정렬
		applySorting(query, pageable);
		List<MemberPageResponse> responses = query
			.where(cursorUserNm(request.getCursorId()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1) // 페이징 + 다음 페이지 존재 여부 확인을 위해 +1
			.fetch();

		boolean hasNext = false;
		if (responses.size() > pageable.getPageSize()) {
			responses.remove(pageable.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(responses, pageable, hasNext);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(member)
			.where(predicate);
	}

	private BooleanExpression createPredicate(MemberSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

		if (request.getUserNm() != null) {
			predicate = predicate.and(member.userNm.eq(request.getUserNm()));
		}

		if (request.getFromCreatedOn() != null) {
			predicate = predicate.and(
				member.createdAndModified.createdOn.goe(request.getFromCreatedOn())); //크거나 같고(.goe)
		}

		if (request.getToCreatedOn() != null) {
			predicate = predicate.and(member.createdAndModified.createdOn.loe(request.getToCreatedOn())); //작거나 같은(.loe)
		}

		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, member,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

	private BooleanExpression cursorUserNm(String userNm) {
		if (userNm == null) {
			return null;
		}
		return member.userNm.lt(userNm);
	}
	@Override
	public Page<MemberResponse> getAdminPage(MemberSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Member> query = createBaseQuery(predicate)
			.select(member)
			.where(member.userType.eq(UserType.SYS_ADMIN)
				.or(member.userType.eq(UserType.SYS_USER))
			);
		Pageable pageable = request.pageable();

		//정렬
		applySorting(query, pageable);

		List<Member> members = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) // 페이징
			.fetch();

		List<MemberResponse> responses = members.stream()
			.map(Member::toResponse)
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(member.count())
			.where(member.userType.eq(UserType.SYS_ADMIN)
				.or(member.userType.eq(UserType.SYS_USER)));

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<PartnerMemberResponse> getPartnerPage(MemberSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Member> query = createBaseQuery(predicate)
			.select(member)
			.where(predicate.and(member.userType.eq(UserType.PARTNER)));
		Pageable pageable = request.pageable();

		//정렬
		applySorting(query, pageable);

		List<Member> members = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) // 페이징
			.fetch();

		List<PartnerMemberResponse> responses = members.stream()
			.map(Member::toPartnerMemberResponse)
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(member.count())
			.where(predicate.and(member.userType.eq(UserType.PARTNER)));

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}
}

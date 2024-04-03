package co.kr.compig.api.domain.member;

import static co.kr.compig.api.domain.member.QMember.*;
import static co.kr.compig.api.domain.member.QMemberGroup.*;

import java.util.List;

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

import co.kr.compig.api.domain.code.UserType;
import co.kr.compig.api.presentation.member.request.MemberSearchRequest;
import co.kr.compig.api.presentation.member.response.AdminMemberResponse;
import co.kr.compig.api.presentation.member.response.MemberPageResponse;
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

	@Override
	public Member getMemberInfo(String id) {
		return jpaQueryFactory
			.selectFrom(member)
			.leftJoin(member.groups, memberGroup).fetchJoin()
			.where(member.id.eq(id))
			.fetchOne();
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(member)
			.where(predicate);
	}

	private BooleanExpression createPredicate(MemberSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request == null) {
			return predicate;
		}
		if (request.getKeyword() != null) {
			predicate = predicate.and(member.userNm.eq(request.getKeyword()));
		}

		if (request.getUserType() != null) {
			predicate = predicate.and(member.userType.eq(request.getUserType()));
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
	public Page<AdminMemberResponse> getAdminPage(MemberSearchRequest request, Pageable pageable) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<AdminMemberResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(AdminMemberResponse.class,
					member.id,
					member.userNm,
					member.userId,
					member.deptCode,
					member.email,
					member.telNo
				)
			).where(member.userType.eq(UserType.SYS_ADMIN)
				.or(member.userType.eq(UserType.SYS_USER))
			);

		//정렬
		applySorting(query, pageable);

		List<AdminMemberResponse> responses = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) // 페이징
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(member.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}
}

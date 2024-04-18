package co.kr.compig.api.domain.member;

import static co.kr.compig.api.domain.member.QMember.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

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

}

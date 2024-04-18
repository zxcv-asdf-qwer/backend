package co.kr.compig.api.domain.review;

import static co.kr.compig.api.domain.member.QMember.*;
import static co.kr.compig.api.domain.review.QReview.*;

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

import co.kr.compig.api.presentation.review.request.ReviewSearchRequest;
import co.kr.compig.api.presentation.review.response.ReviewResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Slice<ReviewResponse> findAllByCondition(ReviewSearchRequest reviewSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(reviewSearchRequest);
		JPAQuery<ReviewResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(ReviewResponse.class,
				review.id,
				member.userNm,
				review.createdAndModified.createdOn,
				review.contents,
				review.point
			))
			.leftJoin(review.member, member);

		applySorting(query, pageable);

		List<ReviewResponse> reviews = query.where(
				cursorCursorId(Long.valueOf(reviewSearchRequest.getCursorId())))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = false;
		if (reviews.size() > pageable.getPageSize()) {
			reviews.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(reviews, pageable, hasNext);
	}

	private BooleanExpression createPredicate(ReviewSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		predicate = predicate.and(review.careOrder.id.eq(request.getOrderId()));
		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory.from(review).where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, review,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target
			);
			query.orderBy(orderSpecifier);
		}
	}

	private BooleanExpression cursorCursorId(Long cursorId) {
		if (cursorId == null) {
			return null;
		}
		return review.id.lt(cursorId);
	}
}

package co.kr.compig.api.domain.review;

import static co.kr.compig.api.domain.review.QReview.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import com.google.common.base.CaseFormat;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
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
		JPAQuery<Review> query = createBaseQuery(predicate)
			.select(review);

		applySorting(query, pageable);

		List<Review> reviews = query.where(
				cursorCursorId(reviewSearchRequest.getCursorId()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<ReviewResponse> responses = reviews.stream()
			.map(Review::toReview)
			.collect(Collectors.toList());

		boolean hasNext = false;
		if (reviews.size() > pageable.getPageSize()) {
			reviews.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(responses, pageable, hasNext);
	}

	private BooleanExpression createPredicate(ReviewSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getPartnerMemberId() != null) {
			predicate = predicate.and(review.member.id.eq(request.getPartnerMemberId()));
		}
		if (request.getOrderId() != null) {
			predicate = predicate.and(review.careOrder.id.eq(request.getOrderId()));
		}

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

	private BooleanExpression cursorCursorId(String cursorId) {
		if (cursorId == null) {
			return null;
		}
		return review.id.lt(Long.parseLong(cursorId));
	}
}

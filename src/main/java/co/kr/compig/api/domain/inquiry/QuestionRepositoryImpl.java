package co.kr.compig.api.domain.inquiry;

import static co.kr.compig.api.domain.board.QBoard.*;
import static co.kr.compig.api.domain.inquiry.QQuestion.*;

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

import co.kr.compig.api.presentation.inquiry.request.QuestionSearchRequest;
import co.kr.compig.api.presentation.inquiry.response.QuestionResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<QuestionResponse> findPage(QuestionSearchRequest questionSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(questionSearchRequest);

		JPAQuery<QuestionResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(QuestionResponse.class,
					question.id,
					question.questionType,
					question.questionTitle,
					question.createdAndModified.createdOn,
					question.isAnswer
				)
			);

		applySorting(query, pageable);

		List<QuestionResponse> questions = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(question.count());

		return PageableExecutionUtils.getPage(questions, pageable, countQuery::fetchOne);
	}

	@Override
	public Slice<QuestionResponse> findAllByCondition(QuestionSearchRequest questionSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(questionSearchRequest);

		JPAQuery<QuestionResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(QuestionResponse.class,
					question.id,
					question.questionType,
					question.questionTitle,
					question.createdAndModified.createdOn,
					question.isAnswer
				)
			);

		applySorting(query, pageable);

		List<QuestionResponse> questions = query
			.where(cursorCursorId(Long.parseLong(questionSearchRequest.getCursorId())))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1) // 페이징 + 다음 페이지 존재 여부 확인을 위해 +1
			.fetch();

		boolean hasNext = false;
		if (questions.size() > pageable.getPageSize()) {
			questions.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(questions, pageable, hasNext);
	}

	private BooleanExpression createPredicate(QuestionSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		predicate = predicate.and(question.createdAndModified.createdBy.eq(request.getMemberId()));
		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(question)
			.where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, question,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

	private BooleanExpression cursorCursorId(Long cursorId) {
		if (cursorId == null)
			return null;
		return board.id.lt(cursorId);
	}
}

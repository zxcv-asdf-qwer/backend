package co.kr.compig.api.domain.inquiry;

import static co.kr.compig.api.domain.inquiry.QQuestion.*;

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
	public Page<QuestionResponse> getQuestionPage(QuestionSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Question> query = createBaseQuery(predicate)
			.select(question);
		Pageable pageable = request.pageable();

		applySorting(query, pageable);

		List<Question> questions = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<QuestionResponse> responses = questions.stream()
			.map(Question::toQuestionResponse)
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(question.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	@Override
	public Slice<QuestionResponse> getQuestionSlice(QuestionSearchRequest questionSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(questionSearchRequest);

		JPAQuery<Question> query = createBaseQuery(predicate)
			.select(question)
			.where(cursorCursorId(Long.valueOf(questionSearchRequest.getCursorId())));

		applySorting(query, pageable);

		List<Question> questions = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<QuestionResponse> responses = questions.stream()
			.map(Question::toQuestionResponse)
			.collect(Collectors.toList());

		boolean hasNext = false;
		if (questions.size() > pageable.getPageSize()) {
			questions.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(responses, pageable, hasNext);
	}

	private BooleanExpression createPredicate(QuestionSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getMemberId() != null) {
			predicate = predicate.and(question.createdAndModified.createdBy.id.eq(request.getMemberId()));
		}
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
		return question.id.lt(cursorId);
	}
}

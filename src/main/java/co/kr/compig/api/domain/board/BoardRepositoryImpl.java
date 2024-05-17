package co.kr.compig.api.domain.board;

import static co.kr.compig.api.domain.board.QBoard.*;

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

import co.kr.compig.api.presentation.board.request.BoardSearchRequest;
import co.kr.compig.api.presentation.board.response.BoardResponse;
import co.kr.compig.global.code.UseYn;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<BoardResponse> getBoardPage(BoardSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Board> query = createBaseQuery(predicate)
			.select(board);

		Pageable pageable = request.pageable();

		applySorting(query, pageable);

		List<Board> boards = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<BoardResponse> responses = boards.stream()
			.map(Board::toResponse)
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(board.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(board)
			.where(predicate);
	}

	private BooleanExpression createPredicate(BoardSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		predicate = predicate.and(board.useYn.eq(UseYn.Y));
		if (request.getBoardType() != null) {
			predicate = predicate.and(board.boardType.eq(request.getBoardType()));
		}
		if (request.getContentsType() != null) {
			predicate = predicate.and(board.contentsType.eq(request.getContentsType()));
		}
		if (request.getTitle() != null) {
			predicate = predicate.and(board.title.containsIgnoreCase(request.getTitle()));
		}
		if (request.getSmallTitle() != null) {
			predicate = predicate.and(board.smallTitle.containsIgnoreCase(request.getSmallTitle()));
		}
		if (request.getContents() != null) {
			predicate = predicate.and(board.contents.containsIgnoreCase(request.getContents()));
		}
		if (request.getCreatedBy() != null) {
			predicate = predicate.and(
				board.createdAndModified.createdBy.id.containsIgnoreCase(request.getCreatedBy()));
		}
		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, board,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

	@Override
	public Slice<BoardResponse> getBoardSlice(
		BoardSearchRequest request, Pageable pageable
	) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Board> query = createBaseQuery(predicate)
			.select(board);

		applySorting(query, pageable);

		List<Board> boards = query
			.where(cursorCursorId(request.getCursorId()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<BoardResponse> responses = boards.stream()
			.map(Board::toResponse)
			.collect(Collectors.toList());

		boolean hasNext = false;
		if (responses.size() > pageable.getPageSize()) {
			responses.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(responses, pageable, hasNext);
	}

	private BooleanExpression cursorCursorId(String cursorId) {
		if (cursorId == null)
			return null;
		return board.id.lt(Long.parseLong(cursorId));
	}
}

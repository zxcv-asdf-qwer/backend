package co.kr.compig.domain.board;

import static co.kr.compig.domain.board.QBoard.*;

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

import co.kr.compig.api.board.dto.BoardResponse;
import co.kr.compig.api.board.dto.BoardSearchRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<BoardResponse> findPage(BoardSearchRequest boardSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(boardSearchRequest);

		JPAQuery<BoardResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(BoardResponse.class,
					board.id,
					board.title,
					board.smallTitle,
					board.contents,
					board.boardType,
					board.contentsType,
					board.viewCount,
					board.createdAndModified.createdBy,
					board.startDate,
					board.endDate,
					board.thumbnailImageUrl
				)
			);

		//정렬
		applySorting(query, pageable);

		List<BoardResponse> boards = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) //페이징
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(board.count());

		return PageableExecutionUtils.getPage(boards, pageable, countQuery::fetchOne);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(board)
			.where(predicate);
	}

	private BooleanExpression createPredicate(BoardSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
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
				board.createdAndModified.createdBy.containsIgnoreCase(request.getCreatedBy()));
		}
		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, board,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

	// cursor paging
	@Override
	public Slice<BoardResponse> findAllByCondition(
		BoardSearchRequest boardSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(boardSearchRequest);
		JPAQuery<BoardResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(BoardResponse.class,
					board.id,
					board.title,
					board.smallTitle,
					board.contents,
					board.boardType,
					board.contentsType,
					board.viewCount,
					board.createdAndModified.createdBy,
					board.startDate,
					board.endDate,
					board.thumbnailImageUrl
				)
			);

		applySorting(query, pageable);

		List<BoardResponse> boards = query
			.where(cursorCursorId(boardSearchRequest.getCursorId()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1) // 페이징 + 다음 페이지 존재 여부 확인을 위해 +1
			.fetch();

		boolean hasNext = false;
		if (boards.size() > pageable.getPageSize()) {
			boards.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(boards, pageable, hasNext);
	}

	private BooleanExpression cursorCursorId(Long cursorId) {
		if (cursorId == null)
			return null;
		return board.id.lt(cursorId);
	}
}

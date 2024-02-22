package co.kr.compig.domain.board;

import static co.kr.compig.domain.board.QBoard.board;

import co.kr.compig.api.board.dto.BoardResponse;
import co.kr.compig.api.board.dto.BoardSearchRequest;
import com.google.common.base.CaseFormat;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

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
                board.contents,
                board.boardType
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
    if (request.getTitle() != null) {
      predicate = predicate.and(board.title.containsIgnoreCase(request.getTitle()));
    }
    if (request.getContents() != null) {
      predicate = predicate.and(board.contents.containsIgnoreCase(request.getContents()));
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
}

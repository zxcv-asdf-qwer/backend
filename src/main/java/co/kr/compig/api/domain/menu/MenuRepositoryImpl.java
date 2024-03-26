package co.kr.compig.api.domain.menu;

import static co.kr.compig.api.domain.menu.QMenu.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import co.kr.compig.api.presentation.menu.request.MenuSearchRequest;
import co.kr.compig.api.presentation.menu.response.MenuResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<MenuResponse> findPage(MenuSearchRequest menuSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(menuSearchRequest);

		JPAQuery<MenuResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(MenuResponse.class,
				menu.id,
				menu.menuNm));

		applySorting(query, pageable);

		List<MenuResponse> menus = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(menu.count());

		return PageableExecutionUtils.getPage(menus, pageable, countQuery::fetchOne);
	}

	private BooleanExpression createPredicate(MenuSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getMenuDiv() != null) {
			predicate = predicate.and(menu.menuDiv.eq(request.getMenuDiv()));
		}
		if (request.getMenuNm() != null) {
			predicate = predicate.and(menu.menuNm.eq(request.getMenuNm()));
		}
		if (request.getMenuType() != null) {
			predicate = predicate.and(menu.menuType.eq(request.getMenuType()));
		}
		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(menu)
			.where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, menu,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target
			);
			query.orderBy(orderSpecifier);
		}
	}

}

package co.kr.compig.api.domain.permission;

import static co.kr.compig.api.domain.permission.QMenuPermission.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import co.kr.compig.api.presentation.permission.request.MenuPermissionSearchRequest;
import co.kr.compig.api.presentation.permission.response.MenuPermissionResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuPermissionRepositoryImpl implements MenuPermissionRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<MenuPermissionResponse> findPage(MenuPermissionSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<MenuPermission> query = createBaseQuery(predicate)
			.select(menuPermission);

		Pageable pageable = request.pageable();

		applySorting(query, pageable);

		List<MenuPermission> menuPermissions = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<MenuPermissionResponse> responses = menuPermissions.stream()
			.map(MenuPermission::toResponse)
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(menuPermission.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	private BooleanExpression createPredicate(MenuPermissionSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getMenuNm() != null) {
			predicate = predicate.and(menuPermission.menu.menuNm.eq(request.getMenuNm()));
		}
		if (request.getUserNm() != null) {
			predicate = predicate.and(menuPermission.member.userNm.eq(request.getUserNm()));
		}
		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(menuPermission)
			.where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, menuPermission,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target
			);
			query.orderBy(orderSpecifier);
		}
	}
}

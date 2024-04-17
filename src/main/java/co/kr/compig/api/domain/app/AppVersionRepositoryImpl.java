package co.kr.compig.api.domain.app;

import static co.kr.compig.api.domain.app.QAppVersion.*;
import static co.kr.compig.api.domain.apply.QApply.*;
import static co.kr.compig.api.domain.member.QMember.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import com.google.common.base.CaseFormat;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.global.code.AppOsType;
import co.kr.compig.api.presentation.app.response.AppVersionResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppVersionRepositoryImpl implements AppVersionRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<AppVersion> findRecentByOsCode(AppOsType osType) {
		return Optional.ofNullable(jpaQueryFactory
			.selectFrom(appVersion)
			.where(appVersion.osCode.eq(osType))
			.orderBy(appVersion.createdAndModified.createdOn.desc())
			.fetchFirst()
		);
	}

	@Override
	public Page<AppVersionResponse> getAppVersionPage(Pageable pageable) {

		JPAQuery<AppVersion> query = jpaQueryFactory
			.selectFrom(appVersion);

		applySorting(query, pageable);

		List<AppVersion> appVersions = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		List<AppVersionResponse> responses = appVersions.stream()
			.map(AppVersion::toResponse)
			.collect(Collectors.toList());
		JPAQuery<Long> countQuery = jpaQueryFactory.from(member)
			.select(appVersion.count());

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, apply,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}
}

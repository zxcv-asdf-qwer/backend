package co.kr.compig.api.domain.wallet;

import static co.kr.compig.api.domain.wallet.QWallet.*;

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

import co.kr.compig.api.presentation.wallet.request.WalletSearchRequest;
import co.kr.compig.api.presentation.wallet.response.WalletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<WalletResponse> findPage(WalletSearchRequest walletSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(walletSearchRequest);

		JPAQuery<WalletResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(WalletResponse.class,
				wallet.member.id,
				wallet.packing.id));

		applySorting(query, pageable);

		List<WalletResponse> wallets = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(wallet.count());

		return PageableExecutionUtils.getPage(wallets, pageable, countQuery::fetchOne);
	}

	private BooleanExpression createPredicate(WalletSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getUserNm() != null) {
			predicate = predicate.and(wallet.member.userNm.eq(request.getUserNm()));
		}
		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(wallet)
			.where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, wallet,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target
			);
			query.orderBy(orderSpecifier);
		}
	}
}

package co.kr.compig.api.domain.order;

import static co.kr.compig.api.domain.board.QBoard.*;
import static co.kr.compig.api.domain.order.QCareOrder.*;

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

import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.response.CareOrderResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CareOrderRepositoryImpl implements CareOrderRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<CareOrderResponse> findPage(CareOrderSearchRequest careOrderSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(careOrderSearchRequest);

		JPAQuery<CareOrderResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(CareOrderResponse.class,
				careOrder.id,
				careOrder.member.userType,
				careOrder.member.userNm,
				careOrder.member.telNo,
				careOrder.orderPatient.locationType,
				careOrder.orderPatient.address1,
				careOrder.createdAndModified.createdOn
			));

		applySorting(query, pageable);

		List<CareOrderResponse> careOrders = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(careOrder.count());

		return PageableExecutionUtils.getPage(careOrders, pageable, countQuery::fetchOne);
	}

	@Override
	public Slice<CareOrderResponse> findAllByCondition(CareOrderSearchRequest careOrderSearchRequest,
		Pageable pageable) {
		BooleanExpression predicate = createPredicate(careOrderSearchRequest);
		JPAQuery<CareOrderResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(CareOrderResponse.class,
				careOrder.id,
				careOrder.member.userType,
				careOrder.member.userNm,
				careOrder.member.telNo,
				careOrder.orderPatient.locationType,
				careOrder.orderPatient.address1
			));

		applySorting(query, pageable);

		List<CareOrderResponse> careOrders = query
			.where(cursorCursorId(Long.valueOf(careOrderSearchRequest.getCursorId())))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1) // 페이징 + 다음 페이지 존재 여부 확인을 위해 +1
			.fetch();

		boolean hasNext = false;
		if (careOrders.size() > pageable.getPageSize()) {
			careOrders.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(careOrders, pageable, hasNext);
	}

	private BooleanExpression cursorCursorId(Long cursorId) {
		if (cursorId == null)
			return null;
		return board.id.lt(cursorId);
	}

	private BooleanExpression createPredicate(CareOrderSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request == null) {
			return predicate;
		}
		if (request.getPublishYn() != null) {
			predicate = predicate.and(careOrder.publishYn.eq(request.getPublishYn()));
		}
		if (request.getUserNm() != null) {
			predicate = predicate.and(careOrder.member.userNm.eq(request.getUserNm()));
		}
		if (request.getTelNo() != null) {
			predicate = predicate.and(careOrder.member.telNo.eq(request.getTelNo()));
		}
		if (request.getOrderStatus() != null) {
			predicate = predicate.and(careOrder.orderStatus.eq(request.getOrderStatus()));
		}
		if (request.getFromCreatedOn() != null) {
			predicate = predicate.and(
				careOrder.createdAndModified.createdOn.goe(request.getFromCreatedOn())); //크거나 같고(.goe)
		}
		if (request.getToCreatedOn() != null) {
			predicate = predicate.and(
				careOrder.createdAndModified.createdOn.loe(request.getToCreatedOn())); //작거나 같은(.loe)
		}

		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(careOrder)
			.where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, careOrder,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target
			);
			query.orderBy(orderSpecifier);
		}
	}

}

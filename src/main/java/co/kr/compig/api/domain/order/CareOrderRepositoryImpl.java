package co.kr.compig.api.domain.order;

import static co.kr.compig.api.domain.member.QMember.*;
import static co.kr.compig.api.domain.order.QCareOrder.*;

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

import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.response.CareOrderPageResponse;
import co.kr.compig.api.presentation.order.response.UserCareOrderResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CareOrderRepositoryImpl implements CareOrderRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<CareOrderPageResponse> findPage(CareOrderSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<CareOrder> query = createBaseQuery(predicate)
			.select(careOrder);
		Pageable pageable = request.pageable();

		applySorting(query, pageable);

		List<CareOrder> careOrders = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) // 페이징 + 다음 페이지 존재 여부 확인을 위해 +1
			.fetch();

		List<CareOrderPageResponse> responses = careOrders.stream()
			.map(CareOrder::toCareOrderPageResponse)
			.toList();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(careOrder.count())
			.leftJoin(careOrder.member, member);

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	@Override
	public Slice<UserCareOrderResponse> findAllByCondition(CareOrderSearchRequest careOrderSearchRequest,
		Pageable pageable) {
		BooleanExpression predicate = createPredicate(careOrderSearchRequest);
		// JPAQuery<UserCareOrderResponse> query = createBaseQuery(predicate).select(
		// 	Projections.constructor(UserCareOrderResponse.class, careOrder.id, careOrder.orderStatus,
		// 		careOrder.orderType, careOrder.applys.size(),
		// 		careOrder.orderPatient.address1, careOrder.orderPatient.address2, careOrder.packages.any().periodType
		// 		, careOrder.packages.any().amount, careOrder.startDateTime, careOrder.endDateTime));

		JPAQuery<CareOrder> query = createBaseQuery(predicate).select(careOrder);

		applySorting(query, pageable);

		List<CareOrder> careOrders = query.where(
				cursorCursorId(careOrderSearchRequest.getCursorId()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1) // 페이징 + 다음 페이지 존재 여부 확인을 위해 +1
			.fetch();

		List<UserCareOrderResponse> responses = careOrders.stream()
			.map(CareOrder::toUserCareOrderResponse)
			.collect(Collectors.toList());

		boolean hasNext = false;
		if (careOrders.size() > pageable.getPageSize()) {
			careOrders.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(responses, pageable, hasNext);
	}

	private BooleanExpression cursorCursorId(String cursorId) {
		if (cursorId == null)
			return null;
		return careOrder.id.lt(Long.parseLong(cursorId));
	}

	private BooleanExpression createPredicate(CareOrderSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request == null) {
			return predicate;
		}
		if (request.getGuardianMemberId() != null) {
			predicate = predicate.and(careOrder.member.id.eq(request.getGuardianMemberId()));
		}
		if (request.getPartnerMemberId() != null) {
			predicate = predicate.and(careOrder.applys.any().member.id.eq(request.getPartnerMemberId()));
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
		if (request.getOrderType() != null) {
			predicate = predicate.and(careOrder.orderType.eq(request.getOrderType()));
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
		return jpaQueryFactory.from(careOrder).where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, careOrder,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"}) OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

}

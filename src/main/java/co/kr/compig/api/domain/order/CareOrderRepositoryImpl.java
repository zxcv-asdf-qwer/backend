package co.kr.compig.api.domain.order;

import static co.kr.compig.api.domain.apply.QApply.*;
import static co.kr.compig.api.domain.board.QBoard.*;
import static co.kr.compig.api.domain.member.QMember.*;
import static co.kr.compig.api.domain.member.QNoMember.*;
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

import co.kr.compig.api.presentation.apply.response.ApplyCareOrderResponse;
import co.kr.compig.api.presentation.order.request.CareOrderSearchRequest;
import co.kr.compig.api.presentation.order.response.CareOrderDetailResponse;
import co.kr.compig.api.presentation.order.response.CareOrderResponse;
import co.kr.compig.api.presentation.patient.response.OrderPatientDetailResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CareOrderRepositoryImpl implements CareOrderRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<CareOrderDetailResponse> findPage(CareOrderSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<CareOrderDetailResponse> query = createBaseQuery(predicate)
			.leftJoin(careOrder.member, member)
			.leftJoin(careOrder.noMember, noMember)
			.select(
				Projections.constructor(CareOrderDetailResponse.class, careOrder.id,
					member.id,
					noMember.id,
					member.userNm,
					member.telNo,
					careOrder.startDateTime, careOrder.endDateTime, careOrder.orderStatus, careOrder.publishYn,
					careOrder.careOrderProcessType, careOrder.orderRequest,
					Projections.constructor(OrderPatientDetailResponse.class, careOrder.orderPatient.id,
						careOrder.orderPatient.name, careOrder.orderPatient.gender, careOrder.orderPatient.birthDate,
						careOrder.orderPatient.height, careOrder.orderPatient.weight, careOrder.orderPatient.diseaseNms,
						careOrder.orderPatient.selfToiletAvailabilities, careOrder.orderPatient.moveAvailability,
						careOrder.orderPatient.mealAvailability, careOrder.orderPatient.genderPreference,
						careOrder.orderPatient.covid19Test, careOrder.orderPatient.patientRequest,
						careOrder.orderPatient.locationType, careOrder.orderPatient.addressCd,
						careOrder.orderPatient.address1, careOrder.orderPatient.address2,
						careOrder.orderPatient.member.id),
					jpaQueryFactory.select(member.userId)
						.from(member)
						.where(member.id.eq(careOrder.createdAndModified.createdBy)),
					careOrder.createdAndModified.createdOn,
					jpaQueryFactory.select(member.userId)
						.from(member)
						.where(member.id.eq(careOrder.createdAndModified.updatedBy)),
					careOrder.createdAndModified.updatedOn));
		Pageable pageable = request.pageable();

		applySorting(query, pageable);

		List<CareOrderDetailResponse> careOrders = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize()) // 페이징 + 다음 페이지 존재 여부 확인을 위해 +1
			.fetch();
		List<ApplyCareOrderResponse> applies = jpaQueryFactory.from(apply)
			.select(
				Projections.constructor(ApplyCareOrderResponse.class, apply.careOrder.id, apply.id, apply.applyStatus))
			.fetch();
		careOrders.stream()
			.parallel()
			.forEach(order -> order.setApplies(applies.stream()
				.parallel()
				.filter(apply -> apply.getOrderId().equals(order.getOrderId()))
				.toList()));

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(careOrder.count())
			.leftJoin(careOrder.member, member)
			.leftJoin(careOrder.noMember, noMember);

		return PageableExecutionUtils.getPage(careOrders, pageable, countQuery::fetchOne);
	}

	@Override
	public Slice<CareOrderResponse> findAllByCondition(CareOrderSearchRequest careOrderSearchRequest,
		Pageable pageable) {
		BooleanExpression predicate = createPredicate(careOrderSearchRequest);
		JPAQuery<CareOrderResponse> query = createBaseQuery(predicate).select(
			Projections.constructor(CareOrderResponse.class, careOrder.id, careOrder.member.userType,
				careOrder.member.userNm, careOrder.member.telNo, careOrder.orderPatient.locationType,
				careOrder.orderPatient.address1));

		applySorting(query, pageable);

		List<CareOrderResponse> careOrders = query.where(
				cursorCursorId(Long.valueOf(careOrderSearchRequest.getCursorId())))
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
		return jpaQueryFactory.from(careOrder).where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, careOrder,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"}) OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

	private void applySorting(Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, careOrder,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"}) OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);

		}
	}

}

package co.kr.compig.api.domain.hospital;

import static co.kr.compig.api.domain.hospital.QHospital.*;

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

import co.kr.compig.api.presentation.hospital.request.HospitalSearchRequest;
import co.kr.compig.api.presentation.hospital.response.HospitalResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HospitalRepositoryImpl implements HospitalRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<HospitalResponse> findPage(HospitalSearchRequest hospitalSearchRequest,
		Pageable pageable) {
		BooleanExpression predicate = createPredicate(hospitalSearchRequest);

		JPAQuery<HospitalResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(HospitalResponse.class,
					hospital.id,
					hospital.hospitalNm,
					hospital.hospitalCode,
					hospital.hospitalAddress1,
					hospital.hospitalTelNo,
					hospital.hospitalOperationHours
				)
			);

		applySorting(query, pageable);

		List<HospitalResponse> hospitals = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = createBaseQuery(predicate)
			.select(hospital.count());

		return PageableExecutionUtils.getPage(hospitals, pageable, countQuery::fetchOne);
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(hospital)
			.where(predicate);
	}

	private BooleanExpression createPredicate(HospitalSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getHospitalNm() != null) {
			predicate = predicate.and(hospital.hospitalNm.containsIgnoreCase(request.getHospitalNm()));
		}
		return predicate;
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, hospital,
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target
			);
			query.orderBy(orderSpecifier);
		}
	}

	// cursor paging
	@Override
	public Slice<HospitalResponse> findAllByCondition(
		HospitalSearchRequest hospitalSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(hospitalSearchRequest);
		JPAQuery<HospitalResponse> query = createBaseQuery(predicate)
			.select(Projections.constructor(HospitalResponse.class,
					hospital.id,
					hospital.hospitalNm,
					hospital.hospitalCode,
					hospital.hospitalAddress1,
					hospital.hospitalTelNo,
					hospital.hospitalOperationHours
				)
			);

		applySorting(query, pageable);

		List<HospitalResponse> hospitals = query
			.where(cursorCursorId(Long.parseLong(hospitalSearchRequest.getCursorId())))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1) // 페이징 + 다음 페이지 존재 여부 확인을 위해 +1
			.fetch();

		boolean hasNext = false;
		if (hospitals.size() > pageable.getPageSize()) {
			hospitals.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(hospitals, pageable, hasNext);
	}

	private BooleanExpression cursorCursorId(Long cursorId) {
		if (cursorId == null)
			return null;
		return hospital.id.lt(cursorId);
	}
}

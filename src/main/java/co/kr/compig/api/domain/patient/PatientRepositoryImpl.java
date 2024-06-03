package co.kr.compig.api.domain.patient;

import static co.kr.compig.api.domain.patient.QPatient.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import com.google.common.base.CaseFormat;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.api.presentation.patient.request.PatientSearchRequest;
import co.kr.compig.api.presentation.patient.response.PatientResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PatientRepositoryImpl implements PatientRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Slice<PatientResponse> findAllByCondition(PatientSearchRequest patientSearchRequest, Pageable pageable) {
		BooleanExpression predicate = createPredicate(patientSearchRequest);

		JPAQuery<Patient> query = createBaseQuery(predicate).select(patient);

		applySorting(query, pageable);

		List<Patient> patients = query
			.where(cursorCursorId(patientSearchRequest.getCursorId()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<PatientResponse> responses = patients.stream()
			.map(Patient::toPatientResponse)
			.collect(Collectors.toList());

		boolean hasNext = false;
		if (patients.size() > pageable.getPageSize()) {
			patients.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(responses, pageable, hasNext);
	}

	private BooleanExpression createPredicate(PatientSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getMemberId() != null) {
			predicate = predicate.and(patient.member.id.eq(request.getMemberId()));
		}
		return predicate;
	}

	private JPAQuery<?> createBaseQuery(BooleanExpression predicate) {
		return jpaQueryFactory
			.from(patient)
			.where(predicate);
	}

	private void applySorting(JPAQuery<?> query, Pageable pageable) {
		for (Sort.Order order : pageable.getSort()) {
			Path<Object> target = Expressions.path(Object.class, patient,
				CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
			@SuppressWarnings({"rawtypes", "unchecked"})
			OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
				order.isAscending() ? Order.ASC : Order.DESC, target);
			query.orderBy(orderSpecifier);
		}
	}

	private BooleanExpression cursorCursorId(String cursorId) {
		if (cursorId == null)
			return null;
		return patient.id.lt(Long.parseLong(cursorId));
	}
}

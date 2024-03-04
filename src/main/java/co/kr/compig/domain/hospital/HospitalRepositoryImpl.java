package co.kr.compig.domain.hospital;

import static co.kr.compig.domain.hospital.QHospital.hospital;

import co.kr.compig.api.hospital.dto.HospitalResponse;
import co.kr.compig.api.hospital.dto.HospitalSearchRequest;
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
public class HospitalRepositoryImpl implements HospitalRepositoryCustom{
  private final JPAQueryFactory jpaQueryFactory;


  @Override
  public Page<HospitalResponse> findPage(HospitalSearchRequest hospitalSearchRequest,
      Pageable pageable){
    BooleanExpression predicate = createPredicate(hospitalSearchRequest);

    JPAQuery<HospitalResponse>  query = createBaseQuery(predicate)
        .select(Projections.constructor(HospitalResponse.class,
                hospital.id,
                hospital.hospitalNm,
                hospital.hospitalCode,
                hospital.hospitalAddress1,
                hospital.hospitalAddress2,
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

  private JPAQuery<?> createBaseQuery(BooleanExpression predicate){
    return jpaQueryFactory
        .from(hospital)
        .where(predicate);
  }
  private BooleanExpression createPredicate(HospitalSearchRequest request){
    BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
    if(request.getHospitalNm() != null){
      predicate = predicate.and(hospital.hospitalNm.containsIgnoreCase(request.getHospitalNm()));
    }
    return predicate;
  }

  private void applySorting(JPAQuery<?> query, Pageable pageable){
    for(Sort.Order order : pageable.getSort()){
      Path<Object> target = Expressions.path(Object.class, hospital,
          CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, order.getProperty()));
      @SuppressWarnings({"rawtypes", "unchecked"})
      OrderSpecifier<?> orderSpecifier = new OrderSpecifier(
          order.isAscending() ? Order.ASC : Order.DESC, target
      );
      query.orderBy(orderSpecifier);
    }
  }
}

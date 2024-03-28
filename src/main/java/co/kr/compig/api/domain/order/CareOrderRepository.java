package co.kr.compig.api.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CareOrderRepository extends JpaRepository<CareOrder, Long>,
	QuerydslPredicateExecutor<CareOrder> {
}

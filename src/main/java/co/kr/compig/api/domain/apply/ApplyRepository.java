package co.kr.compig.api.domain.apply;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ApplyRepository extends JpaRepository<Apply, Long>,
	QuerydslPredicateExecutor<Apply> {

	Set<Apply> findAllByCareOrderId(Long careOrderId);
}

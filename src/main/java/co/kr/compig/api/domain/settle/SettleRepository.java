package co.kr.compig.api.domain.settle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SettleRepository extends JpaRepository<Settle, Long>,
	QuerydslPredicateExecutor<Settle> {

	List<Settle> findBySettleGroupId(Long settleGroupId);
}

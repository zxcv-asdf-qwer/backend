package co.kr.compig.api.domain.settle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SettleGroupRepository extends JpaRepository<SettleGroup, Long>,
	QuerydslPredicateExecutor<SettleGroup> {

}

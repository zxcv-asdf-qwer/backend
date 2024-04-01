package co.kr.compig.api.domain.packing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PackingRepository extends JpaRepository<Packing, Long>,
	QuerydslPredicateExecutor<Packing> {
}

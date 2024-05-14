package co.kr.compig.api.domain.push;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PushRepository extends JpaRepository<Push, Long>, QuerydslPredicateExecutor<Push> {

}

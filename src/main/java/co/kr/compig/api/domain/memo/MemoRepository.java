package co.kr.compig.api.domain.memo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemoRepository extends JpaRepository<Memo, Long>,
	QuerydslPredicateExecutor<Memo> {
}

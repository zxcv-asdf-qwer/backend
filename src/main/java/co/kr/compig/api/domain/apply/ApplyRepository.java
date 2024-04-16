package co.kr.compig.api.domain.apply;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ApplyRepository extends JpaRepository<Apply, Long>,
	QuerydslPredicateExecutor<Apply> {

	List<Apply> findAllByMemberId(String memberId);
}

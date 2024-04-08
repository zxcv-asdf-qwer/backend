package co.kr.compig.api.domain.terms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TermsRepository extends JpaRepository<Terms, Long>,
	QuerydslPredicateExecutor<Terms> {
}

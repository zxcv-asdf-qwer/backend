package co.kr.compig.api.domain.hospital;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface HospitalRepository extends JpaRepository<Hospital, Long>,
	QuerydslPredicateExecutor<Hospital> {

}

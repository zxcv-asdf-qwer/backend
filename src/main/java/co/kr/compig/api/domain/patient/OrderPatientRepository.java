package co.kr.compig.api.domain.patient;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface OrderPatientRepository extends JpaRepository<OrderPatient, Long>,
	QuerydslPredicateExecutor<OrderPatient> {

	Optional<OrderPatient> findByMemberId(String memberId);
}

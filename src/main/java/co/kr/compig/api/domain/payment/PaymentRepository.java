package co.kr.compig.api.domain.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PaymentRepository extends JpaRepository<Payment, Long>,
	QuerydslPredicateExecutor<Payment> {

	Optional<Payment> findByCareOrderId(Long orderId);
}

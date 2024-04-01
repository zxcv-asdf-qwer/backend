package co.kr.compig.api.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PaymentCancelRepository extends JpaRepository<PaymentCancel, Long>,
	QuerydslPredicateExecutor<PaymentCancel> {
}

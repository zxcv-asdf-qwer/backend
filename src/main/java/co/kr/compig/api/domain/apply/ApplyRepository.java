package co.kr.compig.api.domain.apply;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ApplyRepository extends JpaRepository<Apply, Long>,
	QuerydslPredicateExecutor<Apply> {

	Optional<Apply> findByCareOrder_idAndMember_id(Long orderId, String memberId);

}

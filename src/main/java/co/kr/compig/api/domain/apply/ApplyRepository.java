package co.kr.compig.api.domain.apply;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;

public interface ApplyRepository extends JpaRepository<Apply, Long>,
	QuerydslPredicateExecutor<Apply> {

	Optional<Apply> findByMemberAndCareOrder(Member member, CareOrder careOrder);

}

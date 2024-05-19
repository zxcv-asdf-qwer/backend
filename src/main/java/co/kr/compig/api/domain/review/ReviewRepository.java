package co.kr.compig.api.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.order.CareOrder;

public interface ReviewRepository extends JpaRepository<Review, Long>,
	QuerydslPredicateExecutor<Review> {

	boolean existsByMemberAndCareOrder(Member member, CareOrder careOrder);
}

package co.kr.compig.api.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ReviewRepository extends JpaRepository<Review, Long>,
	QuerydslPredicateExecutor<Review> {
}

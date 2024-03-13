package co.kr.compig.api.domain.inquiry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AnswerRepository extends JpaRepository<Answer, Long>,
	QuerydslPredicateExecutor<Question> {
	Optional<Answer> findByQuestion(Question question);
}

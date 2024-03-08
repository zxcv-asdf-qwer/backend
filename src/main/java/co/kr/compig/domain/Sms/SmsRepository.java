package co.kr.compig.domain.Sms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SmsRepository extends JpaRepository<Sms, String>,
    QuerydslPredicateExecutor<Sms> {


}

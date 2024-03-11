package co.kr.compig.domain.sms;

import co.kr.compig.common.code.SmsTemplateType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SmsTemplateRepository extends JpaRepository<SmsTemplate, String>,
    QuerydslPredicateExecutor<SmsTemplate> {

  Optional<SmsTemplate> findBySmsTemplateType(SmsTemplateType smsTemplateType);
}

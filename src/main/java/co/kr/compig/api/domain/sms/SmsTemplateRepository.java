package co.kr.compig.api.domain.sms;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.global.code.SmsTemplateType;

public interface SmsTemplateRepository extends JpaRepository<SmsTemplate, Long>,
	QuerydslPredicateExecutor<SmsTemplate> {

	Optional<SmsTemplate> findBySmsTemplateType(SmsTemplateType smsTemplateType);
}

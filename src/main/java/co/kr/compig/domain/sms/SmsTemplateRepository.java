package co.kr.compig.domain.sms;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.common.code.SmsTemplateType;

public interface SmsTemplateRepository extends JpaRepository<SmsTemplate, String>,
	QuerydslPredicateExecutor<SmsTemplate> {

	Optional<SmsTemplate> findBySmsTemplateType(SmsTemplateType smsTemplateType);
}
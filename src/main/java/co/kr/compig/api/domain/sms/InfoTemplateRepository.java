package co.kr.compig.api.domain.sms;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.global.code.infoTemplateType;

public interface InfoTemplateRepository extends JpaRepository<InfoTemplate, Long>,
	QuerydslPredicateExecutor<InfoTemplate> {

	Optional<InfoTemplate> findTopByInfoTemplateTypeOrderByIdDesc(infoTemplateType infoTemplateType);
}

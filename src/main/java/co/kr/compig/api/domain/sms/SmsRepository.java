package co.kr.compig.api.domain.sms;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SmsRepository extends JpaRepository<Sms, String>,
	QuerydslPredicateExecutor<Sms> {

	Optional<Sms> findTopByReceiverPhoneNumberAndInfoTemplateOrderByIdDesc(String receiverPhoneNumber,
		InfoTemplate infoTemplate);

	Optional<Sms> findByRefkey(String refKey);
}

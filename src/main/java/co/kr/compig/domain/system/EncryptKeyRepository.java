package co.kr.compig.domain.system;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.common.code.EncryptTarget;

public interface EncryptKeyRepository extends JpaRepository<EncryptKey, Long>,
	QuerydslPredicateExecutor<EncryptKey> {

	Optional<EncryptKey> findByEncryptTarget(EncryptTarget encryptTarget);
}

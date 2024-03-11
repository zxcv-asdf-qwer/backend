package co.kr.compig.domain.system;

import co.kr.compig.common.code.EncryptTarget;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface EncryptKeyRepository extends JpaRepository<EncryptKey, Long>,
    QuerydslPredicateExecutor<EncryptKey> {

  Optional<EncryptKey> findByEncryptTarget(EncryptTarget encryptTarget);
}

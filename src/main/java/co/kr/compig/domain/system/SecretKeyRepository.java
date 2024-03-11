package co.kr.compig.domain.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SecretKeyRepository extends JpaRepository<EncryptKey, String>,
    QuerydslPredicateExecutor<EncryptKey> {

}

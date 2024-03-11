package co.kr.compig.domain.system;

import co.kr.compig.common.code.SystemServiceType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AccessKeyRepository extends JpaRepository<AccessKey, String>,
    QuerydslPredicateExecutor<AccessKey> {

  List<AccessKey> findBySystemServiceTypeOrderByIdDesc(SystemServiceType systemServiceType);


}

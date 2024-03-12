package co.kr.compig.domain.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.common.code.SystemServiceType;

public interface AccessKeyRepository extends JpaRepository<AccessKey, String>,
	QuerydslPredicateExecutor<AccessKey> {

	List<AccessKey> findBySystemServiceTypeOrderByIdDesc(SystemServiceType systemServiceType);

}

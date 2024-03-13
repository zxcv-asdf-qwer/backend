package co.kr.compig.api.domain.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long>, QuerydslPredicateExecutor<AppVersion> {
}

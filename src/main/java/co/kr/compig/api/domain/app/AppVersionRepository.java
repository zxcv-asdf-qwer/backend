package co.kr.compig.api.domain.app;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long>, QuerydslPredicateExecutor<AppVersion> {

	Optional<AppVersion> findByLastVer(Integer lastVer);

	Optional<AppVersion> findByLastVerNm(String lastVerNm);
}

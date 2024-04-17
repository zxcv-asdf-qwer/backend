package co.kr.compig.api.domain.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.global.code.AppOsType;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long>, QuerydslPredicateExecutor<AppVersion> {
	Boolean existsByOsCodeAndLastVerAndMinVer(AppOsType osType, String lastVer, String minVer);
}

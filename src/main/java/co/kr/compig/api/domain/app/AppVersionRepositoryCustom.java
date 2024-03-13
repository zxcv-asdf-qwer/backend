package co.kr.compig.api.domain.app;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import co.kr.compig.api.domain.code.AppOsType;

@Repository
public interface AppVersionRepositoryCustom {

	Optional<AppVersion> findRecentByOsCode(AppOsType osType);
}

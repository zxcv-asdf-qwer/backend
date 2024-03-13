package co.kr.compig.api.domain.app;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface AppVersionRepositoryCustom {

	Optional<AppVersion> findByRecentVersion();
}

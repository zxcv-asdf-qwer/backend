package co.kr.compig.api.domain.app;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

	Optional<AppVersion> findByLastVer(Integer lastVer);

	Optional<AppVersion> findByLastVerNm(String lastVerNm);
}

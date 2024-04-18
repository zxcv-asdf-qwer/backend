package co.kr.compig.api.domain.app;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import co.kr.compig.api.presentation.app.response.AppVersionResponse;
import co.kr.compig.global.code.AppOsType;
import co.kr.compig.global.dto.pagination.PageableRequest;

@Repository
public interface AppVersionRepositoryCustom {

	Optional<AppVersion> findRecentByOsCode(AppOsType osType);

	Page<AppVersionResponse> getAppVersionPage(PageableRequest pageable);

}
